'use strict';
const {describe, it, beforeEach} = require('node:test');
const assert = require('node:assert/strict');
const {createEnv, SEND, VALIDATED, pending} = require('./helpers/env');

const VOTE = '#vote-main';
const STEP1 = '#domain-validation .modal-domain-validation-phone-step1';
const STEP2 = '#domain-validation .modal-domain-validation-phone-step2';
const VALIDATE_CODE = '#validatePhoneCodeDomain-modal-form-button-id';
const {COOKIE} = require('./helpers/common');

const votedOk = (votes) => ({success: true, message: 'YOUR VOTE HAS BEEN REGISTERED', vote: {votes, contestApplicationId: 9}});
const ALREADY_VOTED = {success: false, message: 'YOU HAVE ALREADY VOTED IN THIS APPLICATION', vote: null};

const clickVote = (env) => env.click(VOTE);
const answerLast = (env, payload) => { env.respond(env.lastAjaxIndex(), payload); env.tick(300); };
/** VOTE -> anonymous check -> the phone is pending -> the modal opens on the phone step */
function openVoteModal(env) {
    clickVote(env);
    assert.equal(env.lastAjax().opts.url, '/anon-check');
    answerLast(env, pending({phone: true}));
}
const smsFor = (phone) => Object.assign({}, SEND.smsFallback, {validationPhoneNumber: phone});
/** A new voter completes the whole process with a phone the server did not know: phone -> code -> vote */
function voteWithNewPhone(env, phone, voteResponse) {
    openVoteModal(env);
    env.sendPhone(phone);
    env.respond(env.lastAjaxIndex(), smsFor(phone));
    env.fill('#phoneCode', '0000');
    env.click(VALIDATE_CODE);
    assert.equal(env.lastAjax().opts.url, '/validatePhone');
    env.respond(env.lastAjaxIndex(), VALIDATED);          // correct code -> the pending action is to vote
    assert.equal(env.lastAjax().opts.url, '/check');
    env.respond(env.lastAjaxIndex(), VALIDATED);          // the check right before the vote
    assert.equal(env.lastAjax().opts.url, '/ajax/contest/vote');
    env.respond(env.lastAjaxIndex(), voteResponse);
    env.tick(2000);
}
/** What the next voter must find */
function assertModalIsPristine(env) {
    for (const f of ['#phoneNumber', '#phoneNumber2', '#phoneCode', '#phoneHash', '#validationPhoneNumber', '#validationPhoneNumberPrefix']) {
        assert.equal(env.val(f), '', `${f} should be empty`);
    }
    assert.equal(env.displayed(STEP1), true, 'step 1 visible');
    assert.equal(env.displayed(STEP2), false, 'code step hidden');
    assert.equal(env.u.lastValidatedPhone, '');
    assert.equal(env.u.currentChannel, '');
    assert.equal(env.u.lastValidatedHash, '');
    assert.equal(env.u.validated, false);
    assert.equal(env.text('.modal-domain-validation-phone-step2 .help-block'), 'HELP-WA');
    assert.equal(env.H.pendingTimers(), 0, 'no timers should be left alive');
}
const voteButtonIsEnabled = (env) => env.attr(VOTE, 'btn-disabled') === 'false' && !env.$(VOTE).hasClass('disabled');

describe('Contest application voting (real contestApplication.js + modal-validation.js)', () => {
    let env;
    beforeEach(() => { env = createEnv({contestApplication: true}); });

    describe('an anonymous voter with a new phone', () => {
        beforeEach(() => voteWithNewPhone(env, '600', votedOk(1)));

        it('the vote is registered and the notice and the new vote count are shown', () => {
            assert.ok(env.has('display.success YOUR VOTE HAS BEEN REGISTERED'));
            assert.equal(env.text(`${VOTE} .number`), '1');
            assert.equal(env.text('.leader-post-stats .number'), '1');
        });

        it('the modal closes and is left as new', () => {
            assert.equal(env.step(), 'modal closed');
            assertModalIsPristine(env);
        });

        it('the anonymous identity of the voter is destroyed', () => {
            assert.ok(env.count(COOKIE) >= 1);
        });

        it('the VOTE button stays enabled for the next person and the page is not left loading', () => {
            assert.equal(voteButtonIsEnabled(env), true);
            assert.equal(env.$('html').hasClass('loading'), false);
        });
    });

    describe('several voters in a row from the same browser without reloading', () => {
        it('each one finds the modal empty and their vote is counted', () => {
            for (const [i, phone] of ['600111111', '600222222', '600333333'].entries()) {
                openVoteModal(env);
                assert.equal(env.step(), 'step 1 (phone)', `voter ${i + 1}: starts on step 1`);
                assert.equal(env.val('#phoneNumber'), '', `voter ${i + 1}: empty phone`);
                assert.equal(env.val('#phoneHash'), '');
                assert.equal(env.u.lastValidatedPhone, '');
                // leaves this modal and really votes (the whole flow starts again with VOTE)
                env.u.silentLogout();
                env.tick(400);
                voteWithNewPhone(env, phone, votedOk(i + 1));
                assert.equal(env.text(`${VOTE} .number`), String(i + 1), `voter ${i + 1}: vote counted`);
                assertModalIsPristine(env);
            }
        });

        it('a voter who leaves the modal without voting passes neither their phone nor their cookie on to the next one', () => {
            openVoteModal(env);
            env.sendPhone('600999999');
            env.respond(env.lastAjaxIndex(), smsFor('600999999'));
            env.tick(100);
            assert.equal(env.step(), 'step 2 (code)');
            env.$('#domain-validation').attr('data-shown', '0').data('isShown', false); // closes the modal without voting
            const cookiesBefore = env.count(COOKIE);
            clickVote(env);                                                              // the next voter arrives
            assert.equal(env.count(COOKIE), cookiesBefore + 1, 'the cookie of the previous voter is removed before starting');
            assertModalIsPristine(env);
            assert.equal(env.lastAjax().opts.url, '/anon-check');
        });
    });

    describe('a voter whose phone the server already had validated and who had already voted', () => {
        beforeEach(() => {
            openVoteModal(env);
            env.sendPhone('600');
            env.respond(env.lastAjaxIndex(), SEND.alreadyValidated);   // the server sends no code
        });

        it('never sees the code screen and is not sent an SMS or WhatsApp', () => {
            assert.equal(env.displayed(STEP2), false);
            assert.notEqual(env.step(), 'step 2 (code)');
        });

        it('gets the already-voted notice, the modal closes and is left as new', () => {
            env.respond(env.lastAjaxIndex(), VALIDATED);           // check before the vote
            env.respond(env.lastAjaxIndex(), ALREADY_VOTED);       // the backend rejects the second vote
            env.tick(2000);
            assert.ok(env.has('display.error YOU HAVE ALREADY VOTED IN THIS APPLICATION'));
            assert.equal(env.text(`${VOTE} .number`), '0', 'the vote is not counted');
            assert.equal(env.step(), 'modal closed');
            assertModalIsPristine(env);
            assert.ok(env.count(COOKIE) >= 1);
            assert.equal(voteButtonIsEnabled(env), true);
            assert.equal(env.$('html').hasClass('loading'), false);
        });
    });

    describe('errors', () => {
        it('if the anonymous validation check fails: warning, the state is cleaned and the modal does not open', () => {
            clickVote(env);
            env.fail(env.lastAjaxIndex());
            assert.ok(env.has('display.error VALIDATION-ERROR'));
            assert.equal(env.count('modal domain-validation options'), 0, 'the modal does not open');
            assert.ok(env.count(COOKIE) >= 2, 'the cookie is removed at the start and also on failure');
            assert.equal(voteButtonIsEnabled(env), true);
            assert.equal(env.$('html').hasClass('loading'), false);
            // and it can be tried again
            openVoteModal(env);
            assert.equal(env.step(), 'step 1 (phone)');
        });

        it('if the vote request fails: warning, the modal closes and is left as new', () => {
            openVoteModal(env);
            env.sendPhone('600');
            env.respond(env.lastAjaxIndex(), smsFor('600'));
            env.fill('#phoneCode', '0000');
            env.click(VALIDATE_CODE);
            env.respond(env.lastAjaxIndex(), VALIDATED);
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.equal(env.lastAjax().opts.url, '/ajax/contest/vote');
            env.fail(env.lastAjaxIndex());
            env.tick(2000);
            assert.ok(env.has('display.error Error saving vote'));
            assert.equal(env.step(), 'modal closed');
            assertModalIsPristine(env);
            assert.equal(voteButtonIsEnabled(env), true);
        });

        it('a wrong code neither closes the modal nor loses the phone: it can be retried', () => {
            openVoteModal(env);
            env.sendPhone('600');
            env.respond(env.lastAjaxIndex(), smsFor('600'));
            env.fill('#phoneCode', '1111');
            env.click(VALIDATE_CODE);
            env.respond(env.lastAjaxIndex(), {success: false, validated: false, msg: 'WRONG CODE'});
            env.tick(2000);
            assert.equal(env.step(), 'step 2 (code)');
            assert.equal(env.u.lastValidatedPhone, '600');
            assert.equal(env.count(COOKIE), 1, 'only the one at the start: no "logout" while the voter is still in the modal');
        });
    });

    describe('preconditions to vote', () => {
        it('out of time: it warns and starts no validation', () => {
            env.voteButton({'data-deadLineVotesTimeStamp': '1'});
            clickVote(env);
            assert.ok(env.has('display.error OUT OF TIME'));
            assert.equal(env.ajaxCalls.length, 0);
        });

        it('if the button is already disabled (already voted with their account) it does nothing', () => {
            env.voteButton({'btn-disabled': 'true'});
            clickVote(env);
            assert.equal(env.ajaxCalls.length, 0);
        });

        it('with votes pending acceptance of the conditions it does not start the validation', () => {
            env.voteButton({'data-contestRemainingVotes': '1'});
            clickVote(env);
            assert.equal(env.ajaxCalls.length, 0);
        });

        it('if the campaign requires registration and there is no session it opens the registration modal and resets nothing', () => {
            env.voteButton({'data-allowAnonymousAction': 'false'});
            clickVote(env);
            assert.equal(env.ajaxCalls.length, 0);
            assert.ok(env.has('modal registro options') || env.has('modal registro show'));
            assert.equal(env.attr('#registro form', 'callback'), 'contestApplicationVoteNoLogged');
            assert.equal(env.count(COOKIE), 0);
        });
    });

    describe('a registered (non anonymous) user', () => {
        /** Clicking VOTE does a validation check, runs the pending action (which does another check) and then votes */
        function voteAsRegisteredUser(votes) {
            clickVote(env);
            assert.equal(env.lastAjax().opts.url, '/check');
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.equal(env.lastAjax().opts.url, '/check');
            env.respond(env.lastAjaxIndex(), VALIDATED);
            assert.equal(env.lastAjax().opts.url, '/ajax/contest/vote');
            env.respond(env.lastAjaxIndex(), votedOk(votes));
            env.tick(2000);
        }
        beforeEach(() => env.voteButton({'data-loggedUser': 'pedro', 'data-contestRemainingVotes': '0'}));

        it('votes without going through the phone modal or the anonymous check', () => {
            voteAsRegisteredUser(3);
            assert.equal(env.count('modal domain-validation options'), 0, 'the modal never opens');
            assert.ok(env.ajaxCalls.every(c => c.opts.url !== '/anon-check'));
            assert.equal(env.count(COOKIE) >= 1, true);
        });

        it('when voting the button is disabled (one vote per registered person) and the counter is updated', () => {
            voteAsRegisteredUser(3);
            assert.equal(env.attr(VOTE, 'btn-disabled'), 'true');
            assert.ok(env.$(VOTE).hasClass('disabled'));
            assert.equal(env.attr(VOTE, 'data-contestRemainingVotes'), '0');
            assert.equal(env.text(`${VOTE} .number`), '3');
        });
    });
});
