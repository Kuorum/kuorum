package configs

kuorum {
    milestones{
        kuorum = 10
        postVotes{
            ranges=[0,1,2,3,4,5,6,7,8,9,10].collect{power -> [5,10,25].collect{it*(10**power)}}.flatten()//[5,10,25,50,100,250,500,1000,2500,5000,10000,25000]
            publicVotes=10*(10**0) // 10
        }
    }
}