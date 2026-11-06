'use strict';
// handleLoginAndValidationUser: the entry point with login. Only the anonymous branch is contest-specific (kiosk).
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv} = require('../helpers/env');
const {COOKIE} = require('../helpers/common');

describe('Other campaigns - entry point with login (handleLoginAndValidationUser)', () => {
    let env;
    beforeEach(() => { env = createEnv(); });

    it('registered user: goes through the normal check and nothing is reset', () => {
        env.u.handleLoginAndValidationUser(env.button({'data-loggedUser': 'pedro'}), 'cb', env.action());
        assert.equal(env.lastAjax().opts.url, '/check');
        assert.equal(env.count(COOKIE), 0);
    });

    it('no session and anonymous action not allowed: opens the registration modal with the callback and resets nothing', () => {
        env.u.handleLoginAndValidationUser(env.button({'data-allowAnonymousAction': 'false'}), 'myCallback', env.action());
        assert.equal(env.ajaxCalls.length, 0);
        assert.equal(env.attr('#registro form', 'callback'), 'myCallback');
        assert.ok(env.attr('#registro form', 'data-buttonId').startsWith('GUID-'));
        assert.equal(env.count(COOKIE), 0);
    });
});
