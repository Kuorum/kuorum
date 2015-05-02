
var dbDest = dbDest || connect("localhost:27017/Kuorum");


dbDest.project.find().sort({dateCreated:1}).forEach(function(project){

    dbDest.projectEvent.save(
        {
            project:project._id,
            dateCreated:project.dateCreated,
            owner:project.owner,
            projectAction: "PROJECT_CREATED",
            region:project.region
        }
    )
});

