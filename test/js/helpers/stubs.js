// Runs INSIDE the jsdom window, after jQuery and before the code under test.
// Replaces everything that depends on the real browser or the server and keeps a log (H.log) of what happens.
(function () {
    var H = window.H = {log: [], ajaxCalls: []};
    function L() {
        H.log.push([].slice.call(arguments).map(function (a) {
            return typeof a === 'string' ? a : (a === undefined ? 'undefined' : JSON.stringify(a));
        }).join(' '));
    }
    H.L = L;

    // ---- fake timers: the test decides when time passes
    var id = 0, queue = [], now = 0;
    window.setTimeout = function (fn, ms) { id++; queue.push({id: id, t: now + (ms || 0), fn: fn, iv: null}); return id; };
    window.setInterval = function (fn, ms) { id++; queue.push({id: id, t: now + ms, fn: fn, iv: ms}); return id; };
    window.clearTimeout = window.clearInterval = function (i) { queue = queue.filter(function (x) { return x.id !== i; }); };
    H.tick = function (ms) {
        var end = now + ms;
        for (;;) {
            queue.sort(function (a, b) { return a.t - b.t || a.id - b.id; });
            var next = queue[0];
            if (!next || next.t > end) { break; }
            now = next.t;
            if (next.iv) { next.t += next.iv; } else { queue.shift(); }
            next.fn();
        }
        now = end;
    };
    H.pendingTimers = function () { return queue.length; };

    // ---- application globals
    window.console.log = function () { L.apply(null, ['console.log'].concat([].slice.call(arguments))); };
    window.display = {error: function (m) { L('display.error', m); }, success: function (m) { L('display.success', m); }};
    window.pageLoadingOn = function () { $('html').addClass('loading'); };
    window.pageLoadingOff = function () { $('html').removeClass('loading'); };
    window.isPageLoading = function () { return $('html').hasClass('loading'); };
    window.guid = function () { return 'GUID-' + (++id); };
    window.moveSmooth = function () {};
    window.isUserLogged = function () { return !!window.__logged; };
    window.noLoggedCallbacks = {reloadPage: function (m, t) { L('reloadPage', m, t); }};
    window.i18n = {
        inputs: {errors: {nonMatchingPhones: 'NONMATCH'}},
        kuorum: {session: {validation: {error: 'VALIDATION-ERROR', groupError: 'GROUP-ERROR'}},
            web: {commands: {profile: {DomainValidationCommand: {closeWithoutValidation: 'CLOSED-WITHOUT-VALIDATION'}}}}}
    };
    window.kuorumUrls = {domainValidationChecker: '/check'};
    window.cookiesHelper = {removeCookie: function (n) { L('removeCookie', n); }};
    window.grecaptcha = {execute: function (i) { L('grecaptcha.execute', i); }, reset: function (i) { L('grecaptcha.reset', i); }};

    // ---- jQuery: no animations, and a notion of "visible" that jsdom lacks (there is no layout)
    $.fx.off = true;
    function visible(el) {
        while (el && el.nodeType === 1) {
            if (el.style && el.style.display === 'none') { return false; }
            if (el.id === 'domain-validation' && el.getAttribute('data-shown') !== '1') { return false; }
            el = el.parentNode;
        }
        return true;
    }
    H.visible = visible;
    $.expr.pseudos.visible = visible;
    $.expr.pseudos.hidden = function (el) { return !visible(el); };

    // ---- network: every $.ajax is recorded and the test answers whenever it wants
    $.ajax = function (opts) {
        var d = $.Deferred();
        H.ajaxCalls.push({opts: opts, d: d});
        L('ajax', opts.type, opts.url);
        return d.promise();
    };
    H.respond = function (i, payload) {
        var r = H.ajaxCalls[i];
        if (r.opts.success) { r.opts.success(payload, 'success', {}); }
        r.d.resolve(payload);
        if (r.opts.complete) { r.opts.complete({}, 'success'); }
    };
    H.fail = function (i) {
        var r = H.ajaxCalls[i];
        if (r.opts.error) { r.opts.error({status: 500}); }
        r.d.reject({status: 500});
        if (r.opts.complete) { r.opts.complete({}, 'error'); }
    };

    // ---- plugins: jquery-validate and bootstrap modal
    $.fn.valid = function () { return window.__valid !== false; };
    $.fn.rules = function (a, b) { L('rules', this.attr('id'), a, b); return this; };
    $.fn.modal = function (arg) {
        var id = this.attr('id'), $m = this;
        L('modal', id, typeof arg === 'string' ? arg : 'options');
        if (id === 'domain-validation') {
            if (arg === 'hide') {
                if ($m.data('isShown')) {
                    $m.data('isShown', false);
                    $m.trigger('hide.bs.modal');
                    setTimeout(function () { $m.attr('data-shown', '0'); $m.trigger('hidden.bs.modal'); }, 300); // fade-out
                }
            } else {
                $m.data('isShown', true);
                $m.trigger('show.bs.modal');
                setTimeout(function () { $m.attr('data-shown', '1'); $m.trigger('shown.bs.modal'); }, 300); // fade-in
            }
        }
        return this;
    };
})();
