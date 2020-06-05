#Mobile nodes
Mobile_QOIT<-function(path){
Time<-NULL
Nmetric<-NULL
Pnp<-NULL
Score<-NULL
users<-list.files(path=path)
for(u in 1:length(users)){
  path_users<-paste0(path,"/", users[u])
  speed<-list.files(path=path_users)
  for(s in 1:length(speed)){
    user_speed<-paste0(path_users,"/", speed[s]) 
    iterations<-list.files(path=user_speed)
    for(i in 1:length(iterations)){
      user_speed_iter<-paste0(user_speed,"/", iterations[i]) 
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
      time$speed<-word(speed[u])
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
      nm$speed<-word(speed[u])
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
      nm$speed<-word(speed[u])
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
      pnp$speed<-word(speed[s])
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
      pnp$speed<-word(speed[s])
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
      score$speed<-word(speed[s])
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
      score$speed<-word(speed[s])
      score<-as.data.frame(score)
      Score<-rbind(Score,score)
      Score<-as.data.frame(Score)
      
      # qm_weight<-read.csv(paste0(user_speed_iter,"/","QualityMetricWeight.csv"),sep=",", header=TRUE)
      # path_ahp<-read.csv(paste0(user_speed_iter,"/","SrcDstPath_QoI.csv"),sep=",", header=TRUE)
      # path_it<-read.csv(paste0(user_speed_iter,"/","SrcDstPath_QoIT.csv" ),sep=",", header=TRUE)
    }
  }
}

return(list(a=Time, b=Nmetric, c= Pnp, d=Score))
}