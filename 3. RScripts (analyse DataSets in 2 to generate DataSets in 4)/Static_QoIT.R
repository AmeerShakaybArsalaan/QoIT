#Mobile nodes
Static_QOIT<-function(path){
  Time<-NULL
  Nmetric<-NULL
  Pnp<-NULL
  Score<-NULL
  ALLScore<-NULL
  users<-list.files(path=path)
  for(u in 1:length(users)){
    path_users<-paste0(path,"/", users[u])
    iterations<-list.files(path=path_users)
      for(i in 1:length(iterations)){
        user_speed_iter<-paste0(path_users,"/", iterations[i]) 
        metrics_files<-list.files(path=user_speed_iter)
        # Files to opens
        time<-NULL
        overhead_ahp<-read.csv(paste0(user_speed_iter,"/","ComputationOverhead_QoI.csv"),sep=",", header=FALSE)
        colnames(overhead_ahp)<-c("seconds")
        overhead_it<-read.csv(paste0(user_speed_iter,"/","ComputationOverhead_QoIT.csv"),sep=",", header=FALSE)
        colnames(overhead_it)<-c("seconds")
        time$seconds_ahp<-overhead_ahp$seconds
        time$run<-iterations[i]
        time$seconds_it<-overhead_it$seconds
        time$users<-as.numeric(word(users[u]))
        time$speed<-"0"
        time<-as.data.frame(time)
        Time<-rbind(Time, time)
        
        nm_tv<-read.csv(paste0(user_speed_iter,"/","NetworkMetric_ThresholdValues.csv"),sep=",", header=FALSE)
        colnames(nm_tv)<-c("Dst","BW_T","HC_Th","PI_Th","IU_Th")
        nm_ahp<-read.csv(paste0(user_speed_iter,"/","NetworkMetricValues_SelectedSource_QoI.csv"),sep=",", header=FALSE)
        colnames(nm_ahp)<-c("Iteration","Dst","Src","BW","HC","PI","IU")
        nm<-NULL
        nm<-nm_ahp
        nm$run<-iterations[i]
        nm$method<-"ahp"
        nm$users<-as.numeric(word(users[u]))
        nm$speed<-"0"
        nm<-as.data.frame(nm)
        Nmetric<-rbind(Nmetric,nm)
        Nmetric<-as.data.frame(Nmetric)
        nm_qoit<-read.csv(paste0(user_speed_iter,"/","NetworkMetricValues_SelectedSource_QoIT.csv"),sep=",", header=FALSE)
        colnames(nm_qoit)<-c("Iteration","Dst","Src","BW","HC","PI","IU")
        nm<-NULL
        nm<-nm_qoit
        nm$run<-iterations[i]
        nm$method<-"it"
        nm$users<-as.numeric(word(users[u]))
        nm$speed<-"0"
        nm<-as.data.frame(nm)
        Nmetric<-rbind(Nmetric,nm)
        Nmetric<-as.data.frame(Nmetric)
        
        pn_it<-read.csv(paste0(user_speed_iter,"/","PNP_SelectedSources_QoITwrtQoIT.csv"),sep=",", header=FALSE)
        colnames(pn_it)<-c("Iteration","Dst","Src","Priority","Number","Percentage")
        pnp<-NULL
        pnp<-pn_it
        pnp$run<-iterations[i]
        pnp$method<-'it'
        pnp$users<-as.numeric(word(users[u]))
        pnp$speed<-"0"
        pnp<-as.data.frame(pnp)
        Pnp<-rbind(Pnp,pnp)
        Pnp<-as.data.frame(Pnp)
        pn_ahp<-read.csv(paste0(user_speed_iter,"/","PNP_SelectedSources_QoIwrtQoIT.csv" ),sep=",", header=FALSE)
        colnames(pn_ahp)<-c("Iteration","Dst","Src","Priority","Number","Percentage")
        pnp<-NULL
        pnp<-pn_ahp
        pnp$run<-iterations[i]
        pnp$method<-'ahp'
        pnp$users<-as.numeric(word(users[u]))
        pnp$speed<-"0"
        pnp<-as.data.frame(pnp)
        Pnp<-rbind(Pnp,pnp)
        Pnp<-as.data.frame(Pnp)
        
        score_it<-read.csv(paste0(user_speed_iter,"/","QoImetricScore_SelectedSource_QoITwrtQoIT.csv"),sep=",", header=FALSE)
        colnames(score_it)<-c("Iteration","Dst","Src","Accuracy","Completeness","Timeliness","Reliability")
        score<-NULL
        score<-score_it
        score$run<-iterations[i]
        score$method<-'it'
        score$users<-as.numeric(word(users[u]))
        score$speed<-"0"
        score<-as.data.frame(score)
        Score<-rbind(Score,score)
        Score<-as.data.frame(Score)
        score_ahp<-read.csv(paste0(user_speed_iter,"/","QoImetricScore_SelectedSource_QoIwrtQoIT.csv"),sep=",", header=FALSE)
        colnames(score_ahp)<-c("Iteration","Dst","Src","Accuracy","Completeness","Timeliness","Reliability")
        score<-NULL
        score<-score_ahp
        score$run<-iterations[i]
        score$method<-'ahp'
        score$users<-as.numeric(word(users[u]))
        score$speed<-"0"
        score<-as.data.frame(score)
        Score<-rbind(Score,score)
        Score<-as.data.frame(Score)
        
        
        ahp_ahp<-read.csv(paste0(user_speed_iter,"/","QoISelectedSource_QoIScore.csv"),sep=",", header=TRUE)
        ahp_it<-read.csv(paste0(user_speed_iter,"/","QoISelectedSource_QoITScore.csv"),sep=",", header=TRUE)
        it_ahp<-read.csv(paste0(user_speed_iter,"/","QoITSelectedSource_QoIScore.csv" ),sep=",", header=TRUE)
        it_it<-read.csv(paste0(user_speed_iter,"/","QoITSelectedSource_QoITScore.csv"),sep=",", header=TRUE)
        
        allscore<-NULL
        allscore<-ahp_ahp
        colnames(allscore)<-c("Iteration","Dst","Src","Score")
        allscore$method<-"ahp"
        allscore$scoretype<-"ahp"
        allscore<-as.data.frame(allscore)
        ALLScore<-rbind(ALLScore, allscore)
        ALLScore<-as.data.frame(ALLScore)
        
        allscore<-NULL
        allscore<-ahp_it
        colnames(allscore)<-c("Iteration","Dst","Src","Score")
        allscore$method<-"ahp"
        allscore$scoretype<-"it"
        allscore<-as.data.frame(allscore)
        ALLScore<-rbind(ALLScore, allscore)
        ALLScore<-as.data.frame(ALLScore)
        
        allscore<-NULL
        allscore<-it_ahp
        colnames(allscore)<-c("Iteration","Dst","Src","Score")
        allscore$method<-"it"
        allscore$scoretype<-"ahp"
        allscore<-as.data.frame(allscore)
        ALLScore<-rbind(ALLScore, allscore)
        ALLScore<-as.data.frame(ALLScore)
        
        
        allscore<-NULL
        allscore<-it_it
        colnames(allscore)<-c("Iteration","Dst","Src","Score")
        allscore$method<-"it"
        allscore$scoretype<-"it"
        allscore<-as.data.frame(allscore)
        ALLScore<-rbind(ALLScore, allscore)
        ALLScore<-as.data.frame(ALLScore)
        
      }
    }
  
  
  return(list(a=Time, b=Nmetric, c= Pnp, d=Score,e=ALLScore))
}