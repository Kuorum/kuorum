'use strict';
// Campaign group validation: runs after the regular validation check.
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, VALIDATED} = require('../helpers/env');
const {pane} = require('../helpers/common');

describe('Other campaigns - group validation', () => {
    let env;
    const groupButton = () => env.button({'data-campaignGroupValidationActive': '/group'});
    beforeEach(() => { env = createEnv(); });

    it('checks the group after the validation check and, if the user belongs to it, runs the action', () => {
        env.u.executeClickButtonHandlingValidations(groupButton(), env.action());
        env.respond(env.lastAjaxIndex(), VALIDATED);                 // validation check
        assert.equal(env.lastAjax().opts.url, '/group');
        env.respond(env.lastAjaxIndex(), {belongsToCampaignGroup: true});
        assert.ok(env.has('action executed PARAMS'));
    });

    it('if the user does not belong to the group it opens the modal with the group notice and does not run the action', () => {
        env.u.executeClickButtonHandlingValidations(groupButton(), env.action());
        env.respond(env.lastAjaxIndex(), VALIDATED);
        env.respond(env.lastAjaxIndex(), {belongsToCampaignGroup: false});
        env.tick(300);
        assert.equal(env.displayed(pane('groupCampaign')), true);
        assert.ok(!env.has('action executed PARAMS'));
        assert.equal(env.count('modal domain-validation options'), 1);
    });
});
