'use strict';

/** Event left in the log when the anonymous voter cookie is removed (kiosk mode only, i.e. contest applications). */
const COOKIE = 'removeCookie KUORUM_USER_UUID';
/** Selector of one of the validation panes of the modal: tokenMail, census, phone, customCode, groupCampaign. */
const pane = (name) => `#domain-validation .modal-domain-validation-${name}`;
/** Tab of the modal steps header. */
const tab = (name) => `#domain-validation .modal-domain-validation-step-tabs li.modal-domain-validation-step-tabs-${name}`;
/** Objects created inside jsdom belong to another "realm": normalize them before comparing with deepEqual. */
const plain = (value) => JSON.parse(JSON.stringify(value));

module.exports = {COOKIE, pane, tab, plain};
