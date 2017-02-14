(function () {
    var isModalOpen = false;
    $('#notitications-list-link').click(function (event) {
        var $notificationListSelector = $(event.target).closest('ul');
        var $badgeSelector = $($notificationListSelector.find('.badge')[0]);
        var $newNotification = $notificationListSelector.find('li.new');
        
        isModalOpen = !isModalOpen;
        
        if (isModalOpen) {
            $badgeSelector.remove();
        } else {
            console.log($newNotification);
            $newNotification.removeClass('new');
        }
    });

    $('#see-more-notifications').click(function (event) {
        event.preventDefault();
        event.stopPropagation();
        console.log(event.target);
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