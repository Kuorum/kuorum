var dbDest = dbDest || connect("localhost:27017/Kuorum");

dbDest.kuorumUser.remove({email:/.*@mail.ru$/})
dbDest.kuorumUser.remove({email:/.*@sina.com$/})
dbDest.kuorumUser.remove({email:/.*@taidar.ru$/})
dbDest.kuorumUser.remove({email:/.*@yandex.com$/})
dbDest.kuorumUser.remove({email:/.*@dressirovkashchenkov.ru$/})