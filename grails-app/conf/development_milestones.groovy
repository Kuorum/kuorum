package configs
kuorum {
    milestones{
        kuorum = 100
        postVotes{
            ranges=[0..<5].withEagerDefault{index ->
                [5,10,25][(index-1)%3]*(10**((new Double((index-1)/3)).trunc())) ..<[5,10,25][(index)%3]*(10**((new Double(index/3)).trunc()))
            }//[[0..5),[5..10),[10..25),[25..50),[50..10),[100..250),[250..500),....]
            publicVotes=10
        }
    }
}