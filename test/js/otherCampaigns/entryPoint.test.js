'use strict';
// Entry point used by debates, surveys, posts, events, petitions...: executeClickButtonHandlingValidations.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, VALIDATED, pending} = require('../helpers/env');
const {COOKIE, plain} = require('../helpers/common');

describe('Other campaigns - entry point (executeClickButtonHandlingValidations)', () => {
    let env;
    beforeEach(() => { env = createEnv(); });

    describe('validation not active', () => {
        it('runs the action directly, without any request', () => {
            env.u.executeClickButtonHandlingValidations(env.button({'data-campaignValidationActive': 'false'}), env.action());
            assert.equal(env.ajaxCalls.length, 0);
            assert.ok(env.has('action executed PARAMS'));
        });
    });

    describe('validation active', () => {
        it('asks the validation check for the campaign', () => {
            env.u.executeClickButtonHandlingValidations(env.button(), env.action());
            assert.equal(env.lastAjax().opts.url, '/check');
            assert.equal(env.lastAjax().opts.type, 'POST');
            assert.equal(env.lastAjax().opts.data.campaignId, '3517');
        });

        it('if the user is already validated it runs the action without opening the modal', () => {
            env.u.executeClickButtonHandlingValidations(env.button(), env.action());
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.ok(env.has('action executed PARAMS'));
            assert.equal(env.count('modal domain-validation options'), 0);
            assert.equal(env.u.validated, true);
        });

        it('if the validation is pending it opens the modal on the phone step and does not run the action', () => {
            env.u.executeClickButtonHandlingValidations(env.button(), env.action());
            env.respond(env.lastAjaxIndex(), pending({phone: true}));
            env.tick(300);
            assert.equal(env.count('modal domain-validation options'), 1);
            assert.equal(env.step(), 'step 1 (phone)');
            assert.ok(!env.has('action executed PARAMS'));
        });

        it('a network error in the check only warns: it neither removes the cookie nor resets the modal (that is contest-only)', () => {
            env.primeValidation();
            env.fill('#phoneNumber', '600');
            env.u.checkUserValid(env.action());
            env.fail(env.lastAjaxIndex());
            assert.ok(env.has('display.error VALIDATION-ERROR'));
            assert.equal(env.count(COOKIE), 0);
            assert.equal(env.val('#phoneNumber'), '600');
            assert.equal(env.count('modal domain-validation hide'), 0);
        });
    });
});

describe('Other campaigns - ExcutableFunctionCallback (API used by every campaign)', () => {
    it('runs the function with the parameters it was created with, every time', () => {
        const env = createEnv();
        const calls = [];
        const callback = new env.u.ExcutableFunctionCallback((params) => calls.push(params), {id: 7});
        callback.exec();
        callback.exec();
        assert.deepEqual(plain(calls), [{id: 7}, {id: 7}]);
    });
});
