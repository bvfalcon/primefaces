/**
 * __PrimeFaces BlockUI Widget__
 *
 * BlockUI is used to block interactivity of Faces components with optional AJAX integration.
 *
 * @prop {JQuery} target The DOM element for the overlay that blocks the UI.
 * @prop {JQuery} content The DOM element for the content of the blocker.
 * @prop {JQuery} blocker The DOM element for the content of the blocking overlay.
 *
 * @interface {PrimeFaces.widget.BlockUICfg} cfg The configuration for the {@link  BlockUI| BlockUI widget}.
 * You can access this configuration via {@link PrimeFaces.widget.BaseWidget.cfg|BaseWidget.cfg}. Please note that this
 * configuration is usually meant to be read-only and should not be modified.
 * @extends {PrimeFaces.widget.BaseWidgetCfg} cfg
 *
 * @prop {number | null} timeout The set-timeout timer ID for the timer of the delay before the AJAX status is
 * triggered.
 * @prop {boolean} cfg.animate When disabled, displays block without animation effect.
 * @prop {boolean} cfg.blocked Blocks the UI by default when enabled.
 * @prop {string} cfg.block Search expression for block targets.
 * @prop {string} cfg.styleClass Style class of the component.
 * @prop {string} cfg.triggers Search expression of the components to bind.
 * @prop {number} cfg.delay Delay in milliseconds before displaying the block. Default is `0`, meaning immediate.
 * @prop {PrimeFaces.UnbindCallback} [resizeHandler] Unbind callback for the resize handler.
 */
PrimeFaces.widget.BlockUI = class BlockUI extends PrimeFaces.widget.BaseWidget {

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    init(cfg) {
        super.init(cfg);

        this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.cfg.block);
        this.content = this.jq;
        this.cfg.animate = this.cfg.animate !== false;
        this.cfg.blocked = !!this.cfg.blocked;

        this.render();

        if (this.cfg.triggers) {
            // #10970 must wait until all components have been rendered and ready
            var $this = this;
            PrimeFaces.queueTask(function() { $this.bindTriggers() }, 1);
        }

        this.bindResizer();

        if (this.cfg.blocked) {
            this.show();
        }
    }

    /**
     * @override
     * @inheritdoc
     * @param {PrimeFaces.PartialWidgetCfg<TCfg>} cfg
     */
    refresh(cfg) {
        this._cleanup();
        super.refresh(cfg);
    }

    /**
     * @override
     * @inheritdoc
     */
    destroy() {
        super.destroy();
        this._cleanup();
    }

    /**
     * Clean up this widget and remove elements from DOM.
     * @private
     */
    _cleanup() {
        this.content.remove();
        this.blocker.remove();
        this.jq.remove();
        this.target.attr('aria-busy', false);
        $(document).off('pfAjaxSend.' + this.id + ' pfAjaxUpdated.' + this.id + ' pfAjaxComplete.' + this.id);
    }

    /**
     * Sets up the global resize listener on the document.
     * @private
     */
    bindResizer() {
        var $this = this;
        this.resizeHandler = PrimeFaces.utils.registerMutationObserver(this, this.target, function() {
            if ($this.isBlocking()) {
                $this.alignOverlay(false);
            }
        });
    }

    /**
      * Sets up the global event listeners on the document.
      * @private
      */
    bindTriggers() {
        var $this = this;

        //listen global ajax send and complete callbacks
        $(document).on('pfAjaxSend.' + this.id, function(e, xhr, settings) {
            if (!$this.cfg.blocked && PrimeFaces.ajax.Utils.isXhrSourceATrigger($this, settings, true)) {
                $this.show();
            }
            else {
                // subscribe to all DOM update events so we can resize even if another DOM element changed
                PrimeFaces.queueTask(function() { $this.alignOverlay() });
            }
        }).on('pfAjaxComplete.' + this.id, function(e, xhr, settings) {
            if (!$this.cfg.blocked && PrimeFaces.ajax.Utils.isXhrSourceATrigger($this, settings, false)) {
                $this.hide();
            }
        }).on('pfAjaxUpdated.' + this.id, function(e, xhr, settings) {
            // subscribe to all DOM update events so we can resize even if another DOM element changed
            if (!$this.cfg.blocked && !PrimeFaces.ajax.Utils.isXhrSourceATrigger($this, settings, true)) {
                PrimeFaces.queueTask(function() { $this.alignOverlay() });
            }
        });
    }

    /**
     * Show the component with optional duration animation.
     *
     * @param {number | string} [duration] Durations are given in milliseconds; higher values indicate slower
     * animations, not faster ones. The strings `fast` and `slow` can be supplied to indicate durations of 200 and 600
     * milliseconds, respectively.
     */
    show(duration) {
        var $this = this;
        if (this.isBlocking()) {
            return;
        }

        var delay = this.cfg.delay || 0;
        this.timeout = PrimeFaces.queueTask(function() {
            // #10484: if delayed and AJAX event already finished
            if (($this.cfg.triggers || delay > 0) && PrimeFaces.ajax.Queue.isEmpty()) {
                PrimeFaces.warn("BlockUI AJAX event completed before showing the block.");
                return;
            }
            $this.alignOverlay();

            var animated = $this.cfg.animate;
            if (animated)
                $this.blocker.fadeIn(duration);
            else
                $this.blocker.show(duration);

            if ($this.hasContent()) {
                if (animated)
                    $this.content.fadeIn(duration);
                else
                    $this.content.show(duration);
            }

            // prevent mouse events
            $this.target.attr('aria-busy', true).css({
                'pointer-events': 'none',
                'user-select': 'none'
            });
            // prevent keyboard focus from entering the blocked area
            $this.target.find(':tabbable').each(function() {
                var $element = $(this);
                var currentTabIndex = $element.attr('tabindex');
                $element.attr('data-bui-tabindex', currentTabIndex || 0).attr('tabindex', -1);
            });
        }, delay);
    }

    /**
     * Hide the component with optional duration animation.
     *
     * @param {number} [duration] Durations are given in milliseconds; higher values indicate slower animations, not
     * faster ones. The strings `fast` and `slow` can be supplied to indicate durations of 200 and 600 milliseconds,
     * respectively.
     */
    hide(duration) {
        if (!this.isBlocking()) {
            return;
        }
        this.deleteTimeout();
        var $this = this;
        var animated = this.cfg.animate;
        var hasContent = this.hasContent();
        var callback = function() {
            if (!hasContent) {
                resetPositionCallback();
            }
        };
        var resetPositionCallback = function() {
            for (let targetElement of $this.target) {
                var currentTarget = $(targetElement);
                var previousPosition = currentTarget.data('p-position');
                if (previousPosition) {
                    currentTarget.css('position', previousPosition);
                }
            }
        };

        if (animated)
            this.blocker.fadeOut(duration, callback);
        else
            this.blocker.hide(duration || 0, callback);

        if (hasContent) {
            if (animated)
                this.content.fadeOut(duration, resetPositionCallback);
            else
                this.content.hide(duration || 0, resetPositionCallback);
        }

        // restore mouse events on the target
        this.target.attr('aria-busy', false).css({
            'pointer-events': 'auto',
            'user-select': 'auto'
        });
        // restore keyboard focus on the blocked area
        this.target.find('[data-bui-tabindex]').each(function() {
            var $element = $(this);
            var originalTabIndex = $element.attr('data-bui-tabindex');
            $element.attr('tabindex', originalTabIndex).removeAttr('data-bui-tabindex');
        });
    }

    /**
     * Renders the client-side parts of this widget.
     * @private
     */
    render() {
        var widgetId = this.id,
            shouldClone = this.hasMultipleTargets() && this.hasContent();
        // there can be 1 to N targets
        for (let targetElement of this.target) {
            var currentTarget = $(targetElement),
                currentTargetId = currentTarget.attr('id') || this.id,
                currentContent = this.jq;

            // create a specific blocker for this target
            var currentBlocker = $('<div id="' + widgetId + '_' + currentTargetId + '_blocker" class="ui-blockui ui-widget-overlay ui-helper-hidden"></div>');

            // style the blocker
            if (this.cfg.styleClass) {
                currentBlocker.addClass(this.cfg.styleClass);
            }

            // when more than 1 target need to clone the content for each target
            if (shouldClone) {
                currentContent = currentContent.clone();
                currentContent.attr('id', widgetId + '_' + currentTargetId + '_blockcontent');
            }

            // assign data ids to this widget
            currentBlocker.attr('data-bui-overlay', widgetId);
            currentContent.attr('data-bui-content', widgetId);


            // ARIA 
            currentTarget.attr('aria-busy', this.cfg.blocked);

            // append the blocker to the document 
            $(document.body).append(currentBlocker);
            currentBlocker.append(currentContent);
        }

        // assign all matching blockers to widget
        this.blocker = $('[data-bui-overlay~="' + widgetId + '"]');
        this.content = $('[data-bui-content~="' + widgetId + '"]');

        // set the size and position to match the target
        this.alignOverlay();
    }

    /**
     * Align the overlay so it covers its target component. Updates the size and position of the blocker and content elements
     * to match the target component's dimensions.
     * @param {boolean} [forceUpdate=true] - Whether to force update the overlay dimensions even if they haven't changed
     * @private
     */
    alignOverlay(forceUpdate = true) {
        this.target = PrimeFaces.expressions.SearchExpressionFacade.resolveComponentsAsSelector(this.jq, this.cfg.block);

        //center position of content
        for (var i = 0; i < this.target.length; i++) {
            var currentTarget = $(this.target[i]),
                blocker = $(this.blocker[i]),
                content = $(this.content[i]);

            // configure the target positioning
            var position = currentTarget.css("position");
            if (position !== "fixed" && position !== "absolute") {
                currentTarget.data('p-position', position).css('position', 'relative');
            }

            // set the size and position to match the target
            var height = currentTarget.outerHeight(),
                width = currentTarget.outerWidth(),
                offset = currentTarget.offset();
           
            // Only update blocker CSS if dimensions changed
            var currentBlockerHeight = blocker.height();
            var currentBlockerWidth = blocker.width();
            var currentBlockerOffset = blocker.offset();

            if (forceUpdate || (height !== currentBlockerHeight ||
                width !== currentBlockerWidth ||
                offset.left !== currentBlockerOffset.left ||
                offset.top !== currentBlockerOffset.top)) {

                blocker.css({
                    'height': height + 'px',
                    'width': width + 'px',
                    'left': offset.left + 'px',
                    'top': offset.top + 'px',
                    'z-index': PrimeFaces.nextZindex()
                });
            }

            if (this.hasContent()) {
                var contentHeight = content.outerHeight();
                var contentWidth = content.outerWidth();

                // #9496 if display:none then we need to clone to get its dimensions
                if (content.height() <= 0) {
                    var currentWidth = this.content[i].getBoundingClientRect().width;
                    var styleWidth = currentWidth ? 'width: ' + currentWidth + 'px' : '';
                    var clone = this.content[i].cloneNode(true);
                    clone.style.cssText = 'position: fixed; top: 0; left: 0; overflow: auto; visibility: hidden; pointer-events: none; height: unset; max-height: unset;' + styleWidth;
                    document.body.append(clone);
                    var jqClone = $(clone);
                    contentHeight = jqClone.outerHeight();
                    contentWidth = jqClone.outerWidth();
                    jqClone.remove();
                }

                // Only update content CSS if position changed
                var currentContentLeft = parseFloat(content.css('left')) || 0;
                var currentContentTop = parseFloat(content.css('top')) || 0;
                var newContentLeft = ((blocker.width() - contentWidth) / 2);
                var newContentTop = ((blocker.height() - contentHeight) / 2);
                if (forceUpdate || (newContentLeft !== currentContentLeft || newContentTop !== currentContentTop)) {
                    content.css({
                        'left': newContentLeft + 'px',
                        'top': newContentTop + 'px',
                        'z-index': PrimeFaces.nextZindex()
                    });
                }
            }
        }
    }

    /**
     * Checks whether the blocking overlay contains any content items.
     * @private
     * @return {boolean} `true` if this blocking overlay has got any content, `false` otherwise.
     */
    hasContent() {
        return this.content.contents().length > 0;
    }

    /**
     * Checks whether this blocker has more than 1 target.
     * @private
     * @return {boolean} `true` if this blocker has more than 1 target, `false` otherwise.
     */
    hasMultipleTargets() {
        return this.target.length > 1;
    }

    /**
     * Checks whether this blockUI is currently blocking.
     * @return {boolean} `true` if this blockUI is blocking, or `false` otherwise.
     */
    isBlocking() {
        return this.blocker.is(':visible');
    }

    /**
     * Clears the ste-timeout timer for the delay.
     * @private
     */
    deleteTimeout() {
        clearTimeout(this.timeout);
        this.timeout = null;
    }

}
