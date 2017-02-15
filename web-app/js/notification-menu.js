(function () {
    /** Delete badge number **/
    var isModalOpen = false;
    $('#notitications-list-link').click(function (event) {
        var $notificationListSelector = $(event.target).closest('ul');
        var $badgeSelector = $($notificationListSelector.find('.badge')[0]);
        var $newNotification = $notificationListSelector.find('li.new');
        
        isModalOpen = !isModalOpen;
        
        if (isModalOpen) {
            $badgeSelector.remove();
        } else {
            $newNotification.removeClass('new');
        }
    });

    $('#see-more-notifications').click(function (event) {
        event.preventDefault();
        event.stopPropagation();

        $.ajax({
            type: 'POST',
            url: '/',
            data: {
                notification: true
            },
            success: function(result) {
                console.log('yes')
            },
            error: function() {
                console.log('error');
            }
        });
    });

})();