# Read and Plot Files

time_mobile<-read.table("/QoIT/ProcessedFiles_lessmore/Time_mobile.csv",sep=",",header=TRUE)
nm_mobile<-read.table("/QoIT/ProcessedFiles_lessmore/Nmetric_mobile.csv",sep=",",header=TRUE)
pnp_mobile<-read.table("/QoIT/ProcessedFiles_lessmore/Pnp_mobile.csv",sep=",",header=TRUE)
score_mobile<-read.table("/QoIT/ProcessedFiles_lessmore/Score_mobile.csv",sep=",",header=TRUE)


time_static<-read.table("/QoIT/ProcessedFiles_static/Time_static.csv",sep=",",header=TRUE)
nm_static<-read.table("/QoIT/ProcessedFiles_static/Nmetric_static.csv",sep=",",header=TRUE)
pnp_static<-read.table("/QoIT/ProcessedFiles_static/Pnp_static.csv",sep=",",header=TRUE)
score_static<-read.table("/QoIT/ProcessedFiles_static/Score_static.csv",sep=",",header=TRUE)


time_less<-read.table("/QoIT/ProcessedFiles_lessmore/Time_static.csv",sep=",",header=TRUE)
nm_less<-read.table("/QoIT/ProcessedFiles_lessmore/Nmetric_static.csv",sep=",",header=TRUE)
pnp_less<-read.table("/QoIT/ProcessedFiles_lessmore/Pnp_static.csv",sep=",",header=TRUE)
score_less<-read.table("/QoIT/ProcessedFiles_lessmore/Score_static.csv",sep=",",header=TRUE)

time_same<-read.table("/QoIT/ProcessedFiles_samediff/Same/Time_static.csv",sep=",",header=TRUE)
nm_same<-read.table("/QoIT/ProcessedFiles_samediff/Same/Nmetric_static.csv",sep=",",header=TRUE)
pnp_same<-read.table("/QoIT/ProcessedFiles_samediff/Same/Pnp_static.csv",sep=",",header=TRUE)
score_same<-read.table("/QoIT/ProcessedFiles_samediff/Same/Score_static.csv",sep=",",header=TRUE)


time_diff<-read.table("/QoIT/ProcessedFiles_samediff/Diff/Time_static.csv",sep=",",header=TRUE)
nm_diff<-read.table("/QoIT/ProcessedFiles_samediff/Diff/Nmetric_static.csv",sep=",",header=TRUE)
pnp_diff<-read.table("/QoIT/ProcessedFiles_samediff/Diff/Pnp_static.csv",sep=",",header=TRUE)
score_diff<-read.table("/QoIT/ProcessedFiles_samediff/Diff/Score_static.csv",sep=",",header=TRUE)





#1. The average number of users with all requests fulfilled
library(dplyr)
#this<-pnp_mobile[pnp_mobile$speed%in%"50-90" & pnp_mobile$Src>-1,]
this<-pnp_less
s3_ahp<-this[this$users%in%3 & this$method%in%"ahp",]
s3ahp<-group_by(s3_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s3_it<-this[this$users%in%3 & this$method%in%"it",]
s3it<-group_by(s3_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))
#----s3----------------------------------------------------------------
s30_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s30ahp<-group_by(s30_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s30_it<-this[this$users%in%30 & this$method%in%"it",]
s30it<-group_by(s30_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

par(mfrow = c(2,3), cex.lab=1,cex=1.0, mar=c(4,6,1,1))
per<-c(s3ahp$per,s3it$per,s30ahp$per,s30it$per)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<- boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Extent to which\n required features are met",
        xlab="(a) Static scenario",
        xaxt="n",
        yaxt="n",
        col=c(NA,NA), density=seq(30,30), lty= c(1, 1),
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
 density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)

per<-c(s3ahp$pri,s3it$pri,s30ahp$pri,s30it$pri)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<-boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Avergae Overall priority of\nfulfilled requirements",
        xlab="(b) Static scenario",
        xaxt="n",
        yaxt="n",
        col=c(NA,NA),density=seq(30,30), lty= c(1, 1), 
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)

per<-c(s3ahp$num,s3it$num,s30ahp$num,s30it$num)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<-boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Average number of\n requirements met",
        xlab="(c) Static scenario",
        xaxt="n",
        yaxt="n",
        col=c(NA,NA), density=seq(30,30), lty= c(1, 1),
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)



this<-pnp_mobile[pnp_mobile$speed%in%"50-90" & pnp_mobile$Src>-1,]
#this<-pnp_static
s3_ahp<-this[this$users%in%3 & this$method%in%"ahp",]
s3ahp<-group_by(s3_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s3_it<-this[this$users%in%3 & this$method%in%"it",]
s3it<-group_by(s3_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))
#----s3----------------------------------------------------------------
s30_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s30ahp<-group_by(s30_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s30_it<-this[this$users%in%30 & this$method%in%"it",]
s30it<-group_by(s30_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

per<-c(s3ahp$per,s3it$per,s30ahp$per,s30it$per)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<-boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Extent to which\n required features are met",
        xlab="(d) Mobile scenario",
        xaxt="n",
        yaxt="n", density=seq(30,30), lty= c(1, 1),
        col=c(NA,NA), 
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)

per<-c(s3ahp$pri,s3it$pri,s30ahp$pri,s30it$pri)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<-boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Overall priority of\nfulfilled requirements",
        xlab="(e) Mobile scenario",
        xaxt="n",
        yaxt="n", density=seq(30,30), lty= c(1, 1),
        col=c(NA,NA), 
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)


per<-c(s3ahp$num,s3it$num,s30ahp$num,s30it$num)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<-boxplot(value~label,
        data=d,
        #at=c(1:2, 4:5),
        main="",
        ylab="Average number of\n requirements met",
        xlab="(f) Mobile scenario",
        xaxt="n",
        yaxt="n", density=seq(30,30), lty= c(1, 1),
        col=c(NA,NA), 
        border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("less","more"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)



(median(s3it$pri)-median(s3ahp$pri))/median(s3ahp$pri)
(median(s3it$per)-median(s3ahp$per))/median(s3ahp$per)
(median(s3it$num)-median(s3ahp$num))/median(s3ahp$num)

(median(s30it$pri)-median(s30ahp$pri))/median(s30ahp$pri)
(median(s30it$per)-median(s30ahp$per))/median(s30ahp$per)
(median(s30it$num)-median(s30ahp$num))/median(s30ahp$num)

#2. User usability of 30 nodes static/mobile******************************
# network metrics, bw: 0.3>=, hc=<=8, PI:2>=, IU:5>=
# quality metrics, acc=0.4, rel=0.3, com=0.2,timeliness=0.1
this<-pnp_static[pnp_static$Src>-1,]
#this<-pnp_static
s_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s_ahp_t1<-as.data.frame(table(s_ahp$Num))

s_it<-this[this$users%in%30 & this$method%in%"it",]
s_it_t1<-as.data.frame(table(s_it$Num))

this<-nm_static[nm_static$Src>-1,]
this$BW<-ifelse((this$BW/0.3)>=1, 1, 0)
this$HC<-ifelse((this$HC/8)>1, 0, 1)
this$PI<-ifelse((this$PI/2)>=1, 1, 0)
this$IU<-ifelse((this$IU/5)>=1, 1, 0)
this$count<-rowSums(this[,c(4:7)])
this<-as.data.frame(this)
s_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s_ahp_t2<-as.data.frame(table(s_ahp$count))

s_it<-this[this$users%in%30 & this$method%in%"it",]
s_it_t2<-as.data.frame(table(s_it$count))

# Percentage share in burst
colors=c("purple","orange","red","blue")
par(mfrow = c(1,4), cex.lab=1.0,cex=1, mar=c(4,2,1,1)) 
#layout(matrix(c(1,2,3,4), 1, 4, byrow = TRUE))
labels<-as.character(as.data.frame(s_ahp_t1)$Var1)
par(xpd=TRUE)
pie(s_ahp_t1$Freq/sum(s_ahp_t1$Freq), labels = round((s_ahp_t1$Freq/sum(s_ahp_t1$Freq))*100,1), 
    main = "",col =colors ,xlab="(a) QoI-AHP", lwd=5,
    angle=c( 45, 0, 135, 90), density=seq(10,30,15), lty= c(1, 4, 2, 3))
legend(-1.4,2.7, labels, cex = 1, lwd=2, bty='n', col=colors, title="No. of\nquality metrics met"
       , angle=c( 45, 0, 135, 90), lty= c(1, 4, 2, 3))

par(xpd=TRUE)
pie(s_it_t1$Freq/sum(s_it_t1$Freq), labels = round((s_it_t1$Freq/sum(s_it_t1$Freq))*100,1), 
    main = "",col=colors ,xlab="(b) QoIT",
    angle=c( 45, 0, 135, 90), density=seq(10,30,15), lty= c(1, 4, 2, 3))
legend(-1.4,2.7, labels,  cex = 1, lwd=2, bty='n', col=colors, title="No. of\nquality metrics met"
       , angle=c( 45, 0, 135, 90), lty= c(1, 4, 2, 3))

colors=c("orange","red","darkgreen","blue")
labels<-as.character(as.data.frame(s_ahp_t2)$Var1)
par(xpd=TRUE)
pie(s_ahp_t2$Freq/sum(s_ahp_t2$Freq), labels = round((s_ahp_t2$Freq/sum(s_ahp_t2$Freq))*100,1), 
    main = "",col =colors ,xlab="(c) QoI-AHP",
    angle=c(  0, 135,45, 90), density=seq(10,30,15), lty= c( 4, 2,1, 3))
legend(-1.4,2.7, labels,  cex = 1, lwd=2, bty='n', col=colors, title="No. of\nnetwork metrics met"
       , angle=c(  0, 135,45, 90), lty= c( 4,2, 1, 3))
par(xpd=TRUE)
pie(s_it_t2$Freq/sum(s_it_t2$Freq), labels = round((s_it_t2$Freq/sum(s_it_t2$Freq))*100,1), 
    main = "",col =colors ,xlab="(d) QoIT",
    angle=c(  0, 135,45, 90), density=seq(10,30,15), lty= c( 4, 2,1, 3))
legend(-1.4,2.7, labels,  cex = 1, lwd=2, bty='n', col=colors, title="No. of\nnetwork metrics met"
       , angle=c(  0, 135,45, 90), lty= c( 4,2, 1, 3))

#----time----------------------------------------------------------------
ahp_time<-time_same[time_same$users%in%30,]$seconds_ahp
it_time<-time_same[time_same$users%in%30,]$seconds_it
par(mfrow = c(1,3), cex.lab=1.0,cex=1.0, mar=c(4,4,1,1))
#layout(matrix(c(1,2,3,3), 1, 4, byrow = TRUE))
# Density plots
plot(density(ahp_ahp), col = 'white',main="",yaxt="n", lty=1, lwd=3, xlim=c(0.05,0.6), xlab="QoI-AHP score of the selected nodes\n (a)") # plots the result
lines(density(it_ahp), col = 'blue',main="",yaxt="n", lty=1, lwd=3, xlab="QoI-AHP score of the selected nodes\n (a)") # plots the results
lines(density(ahp_ahp),  col = 'red',main="",yaxt="n", lty=3, lwd=3, xlab="QoI-AHP score of the selected nodes\n (a)") # plots the results
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=3)


label<-"Computation time in seconds\n (b)"
library(EnvStats)
limit<-c(0,0.02)
ecdfPlot(ahp_time,ecdf.lty=3, ecdf.col = 'red',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
ecdfPlot(it_time, ecdf.lty=1, add=TRUE,ecdf.col = 'blue',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
axis(side=2, las=2)
legend("bottomright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=3)


this<-pnp_static
s3_ahp<-this[this$users%in%30 & this$method%in%"ahp" & this$Src >(-1),]
r=unique(s3_ahp$run)
source_nodes_ahp<-NULL
source_nodes_ahp$ZERO<-0
source_nodes_ahp$ONE<-0
source_nodes_ahp$TWO<-0
source_nodes_ahp$THRE<-0
source_nodes_ahp$FOUR<-0
source_nodes_ahp$FIVE<-0
for (i in 1:length(r)){
  this.run<-s3_ahp[s3_ahp$run%in%r[i],]
  source_nodes_ahp$ZERO[i]<-nrow(this.run[this.run$Src==0,])
  source_nodes_ahp$ONE[i]<-nrow(this.run[this.run$Src==1,])
  source_nodes_ahp$TWO[i]<-nrow(this.run[this.run$Src==2,])
  source_nodes_ahp$THREE[i]<-nrow(this.run[this.run$Src==3,])
  source_nodes_ahp$FOUR[i]<-nrow(this.run[this.run$Src==4,])
  source_nodes_ahp$FIVE[i]<-nrow(this.run[this.run$Src==5,])
}
source_nodes_ahp<-as.data.frame(source_nodes_ahp)
s3_it<-this[this$users%in%30 & this$method%in%"it" & this$Src >(-1),]
r=unique(s3_it$run)
source_nodes_it<-NULL
source_nodes_it$ZERO<-0
source_nodes_it$ONE<-0
source_nodes_it$TWO<-0
source_nodes_it$THRE<-0
source_nodes_it$FOUR<-0
source_nodes_it$FIVE<-0
for (i in 1:length(r)){
  this.run<-s3_it[s3_it$run%in%r[i],]
  source_nodes_it$ZERO[i]<-nrow(this.run[this.run$Src==0,])
  source_nodes_it$ONE[i]<-nrow(this.run[this.run$Src==1,])
  source_nodes_it$TWO[i]<-nrow(this.run[this.run$Src==2,])
  source_nodes_it$THREE[i]<-nrow(this.run[this.run$Src==3,])
  source_nodes_it$FOUR[i]<-nrow(this.run[this.run$Src==4,])
  source_nodes_it$FIVE[i]<-nrow(this.run[this.run$Src==5,])
}
source_nodes_it<-as.data.frame(source_nodes_it)


labels<-rep(c("1","2","3","4","5","6"), each=2)
bottleneck_ahp<-c(mean(source_nodes_ahp$ZERO),
          mean(source_nodes_ahp$ONE),
          mean(source_nodes_ahp$TWO),
          mean(source_nodes_ahp$THREE),
          mean(source_nodes_ahp$FOUR),
          mean(source_nodes_ahp$FIVE))
bottleneck_it<-c(mean(source_nodes_it$ZERO),mean(source_nodes_it$ONE),
                 mean(source_nodes_it$TWO),mean(source_nodes_it$THREE),
                  mean(source_nodes_it$FOUR),mean(source_nodes_it$FIVE))
df<-matrix(c(bottleneck_ahp,bottleneck_it), ncol=6, byrow=TRUE)
barplot(df, main="", ylab="Mean selection times", yaxt='n',
        xlab="Source nodes\n (c)", col=c("red","blue"), 
        angle=c( 90, 0), density=seq(30,30), lty= c(1, 2),
        legend = c("QoI-AHP","QoIT"), beside=TRUE)
axis(side=2, las=2)
axis(side=1, at=c(2,5,8,11,14,17),labels=c("1","2","3","4","5","6"))

## 4. The effect of increasing nodes and changing moobility
this<-pnp_less[pnp_less$users%in%3 ,]
pnp_static1<-rbind(this,pnp_static)
node<-NULL
node$user<-0
node$req_ahp<-0
node$req_it<-0
node$run<-0
u<-unique(pnp_static1$users)
r<-unique(pnp_static1$run)
m=0
u<-sort(u)
for(i in 1:length(u))
{
this<-pnp_static1[pnp_static1$users%in%u[i],]
for(j in 1:length(r))
 {
   m=m+1
   it<-this[this$run%in%r[j] & this$method%in%"it",]
   ahp<-this[this$run%in%r[j] & this$method%in%"ahp",]
   node$user[m]<-i
   node$req_ahp[m]<-length(which(ahp$Number==4))/nrow(ahp)*100
   node$req_it[m]<-length(which(it$Number==4))/nrow(it)*100
   node$run[m]<-r[j]
 }
}
node<-as.data.frame(node)

mobile<-NULL
mobile$speed<-0
mobile$req_ahp<-0
mobile$req_it<-0
mobile$run<-0
pnp_mobile$speed<-as.character(pnp_mobile$speed)
u<-as.character(unique(pnp_mobile$speed))
r<-unique(pnp_mobile$run)
m=0
for(i in 1:length(u))
{
  this<-pnp_mobile[pnp_mobile$users%in%"30" & pnp_mobile$speed%in%u[i],]
  for(j in 1:length(r))
  {
    m=m+1
    it<-this[this$run%in%r[j] & this$method%in%"it",]
    ahp<-this[this$run%in%r[j] & this$method%in%"ahp",]
    mobile$speed[m]<-u[i]
    mobile$req_ahp[m]<-length(which(ahp$Number==4))/nrow(ahp)*100
    mobile$req_it[m]<-length(which(it$Number==4))/nrow(it)*100
    mobile$run[m]<-r[j]
  }
}
mobile<-as.data.frame(mobile)

library(dplyr)
node_req<-group_by(node, user) %>%
  summarise(ahp=mean(req_ahp), it=mean(req_it))

mobile_req<-group_by(mobile, speed) %>%
  summarise(ahp=mean(req_ahp), it=mean(req_it))

random<-node_req$ahp
random[1]<-min(node_req$ahp)
random[2]<-max(node_req$it)


par(mfrow = c(1,2), cex.lab=1.0,cex=0.9, mar=c(4,5,1,2))
plot(node_req$user,random, pch=" ", cex=3, col="white",  ylab="Percentage of querying nodes\n with all rquirements met",
xlab="Number of querying nodes\n(a)", xaxt="n", yaxt='n')
axis(side=2, las=2)
axis(side=1, at=c(1:6),labels=c("3","6","12","18","24","30"))
points(node_req$user,node_req$ahp, pch=15, cex=1.5, col="red",type="b")
points(node_req$user,node_req$it, pch=17, cex=1.5, col="blue",type="b")
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), pch=c(15,17),lty=c(NA,NA),
       ncol=1, cex=1, pt.cex=1.5,bty="n", lwd=4)
 dummy<-mobile_req
 mobile_req[2,]<-dummy[4,]
 mobile_req[3,]<-dummy[2,]
 mobile_req[4,]<-dummy[3,]
random<-mobile_req$ahp
random[1]<-min(mobile_req$ahp)
random[2]<-max(mobile_req$it)
plot(1:4,random, pch=NA, cex=3, col="white",  ylab="Percentage of querying mobiles\n with all rquirements met",
     xlab="Speeds\n(b)",xaxt='n', yaxt='n')
axis(side=2, las=2)
axis(side=1, at=c(1:4),labels=c("walking","running","cycling","car driving"))
points(1:4,mobile_req$ahp, pch=15, cex=1.5, col="red",type="b")
points(1:4,mobile_req$it, pch=17, cex=1.5, col="blue", type="b")
legend("bottomleft", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), pch=c(15,17),lty=c(NA,NA),
       ncol=1, cex=1, pt.cex=1.5,bty="n", lwd=4)





# Diff vs Similar -----------------------------
ahp_time1<-time_same[time_same$users%in%30,]$seconds_ahp
it_time1<-time_same[time_same$users%in%30,]$seconds_it

ahp_time<-time_diff[time_diff$users%in%30,]$seconds_ahp
it_time<-time_diff[time_diff$users%in%30,]$seconds_it
par(mfrow = c(1,2), cex.lab=1,cex=0.9, mar=c(4,5,1,1))

#--5----------------------------------------------------
library(dplyr)
#this<-pnp_mobile[pnp_mobile$speed%in%"50-90" & pnp_mobile$Src>-1,]
this<-pnp_same[pnp_same$Src>(-1),]
s3_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s3ahp<-group_by(s3_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s3_it<-this[this$users%in%30 & this$method%in%"it",]
s3it<-group_by(s3_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

this<-pnp_diff[pnp_diff$Src>(-1),]
s30_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s30ahp<-group_by(s30_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s30_it<-this[this$users%in%30 & this$method%in%"it",]
s30it<-group_by(s30_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

per<-c(s3ahp$num,s3it$num,s30ahp$num,s30it$num)
per.label<-c(rep(1:4,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<- boxplot(value~label,
                  data=d,
                  #at=c(1:2, 4:5),
                  main="",
                  ylab="Average number of \n met requirements",
                  xlab="(b) Static scenario",
                  xaxt="n",
                  yaxt="n",
                  col=c(NA,NA), density=seq(30,30), lty= c(1, 1),
                  border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 90, 0)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(1,3),col = c("red","blue"))            # draw the rectangles

axis(side=1, at=c(1.2,3.5), 
     labels=c("Same requirements","Different requirements"))
axis(side=2, las=2)
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(1, 3), cex=0.9,bty="n",lwd=3)
# The second part of the above subfigure
label<-"Computation time in seconds\n (a)"
library(EnvStats)
limit<-c(0,0.02)
ecdfPlot(ahp_time1,  ecdf.lty=3, ecdf.col = 'red',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
ecdfPlot(it_time1,  ecdf.lty=1, add=TRUE,ecdf.col = 'blue',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)

ecdfPlot(ahp_time,  ecdf.lty=2, add=TRUE,ecdf.col = 'orange',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
ecdfPlot(it_time,  ecdf.lty=4, add=TRUE,ecdf.col = 'aquamarine4',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
axis(side=2, las=2)
legend("bottomright", legend=c("QoI-AHP-same req.","QoI-AHP-diff. req","QoIT-same req.","QoIT-diff req."),
       col=c("red","orange","blue","aquamarine4"), lty=c(3,2,1,4),pch=NA,
       ncol=1, cex=1,bty="n", lwd=2)

# SCORE OF THE QOIT AND AHP---QOIAHP SCORE
allscores<-read.table("/QoIT/ProcessedFiles_allscore/AllScore_static.csv",sep=",",header=TRUE)
ahp_ahp<-allscores[allscores$method%in%"ahp" & allscores$scoretype%in%"ahp",]$Score
ahp_it<-allscores[allscores$method%in%"ahp" & allscores$scoretype%in%"it",]$Score

it_ahp<-allscores[allscores$method%in%"it" & allscores$scoretype%in%"ahp",]$Score
it_it<-allscores[allscores$method%in%"it" & allscores$scoretype%in%"it",]$Score

label<-"QoI-AHP score of the selected nodes\n (a)"
library(EnvStats)
limit<-c(0,0.6)
par(mfrow = c(1,2), cex.lab=1.0,cex=1, mar=c(4,5,1,2))
ecdfPlot(ahp_ahp,  ecdf.lty=3, ecdf.col = 'red',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
ecdfPlot(it_ahp,  ecdf.lty=1, add=TRUE,ecdf.col = 'blue',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
axis(side=2, las=2)
legend("bottomright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=2)
limit<-c(0.6,1)
label<-"QoIT score of the selected nodes\n (b)"
ecdfPlot(ahp_it,   ecdf.lty=3, ecdf.col = 'red',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
ecdfPlot(it_it,  ecdf.lty=1, add=TRUE,ecdf.col = 'blue',main="",yaxt="n", ylab="Ecdf", xlab=label, xlim=limit)
axis(side=2, las=2)
legend("bottomright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=2)

# Density plots
plot(density(ahp_ahp), col = 'white',main="",yaxt="n", lty=1, lwd=3, xlim=c(0.05,0.6), xlab="QoI-AHP score of the selected nodes\n (a)") # plots the result
lines(density(it_ahp), col = 'blue',main="",yaxt="n", lty=1, lwd=3, xlab="QoI-AHP score of the selected nodes\n (a)") # plots the results
lines(density(ahp_ahp),  col = 'red',main="",yaxt="n", lty=3, lwd=3, xlab="QoI-AHP score of the selected nodes\n (a)") # plots the results
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=3)

# plot densities
sm.density.compare(mpg, cyl, xlab="QoI-AHP score of the selected nodes")
title(main="")

### The Figure 1 #------------------------------------------------------
allscores<-read.table("/QoIT/ProcessedFiles_allscore/AllScore_static.csv",sep=",",header=TRUE)
ahp_ahp<-allscores[allscores$method%in%"ahp" & allscores$scoretype%in%"ahp",]$Score
ahp_it<-allscores[allscores$method%in%"ahp" & allscores$scoretype%in%"it",]$Score

it_ahp<-allscores[allscores$method%in%"it" & allscores$scoretype%in%"ahp",]$Score
it_it<-allscores[allscores$method%in%"it" & allscores$scoretype%in%"it",]$Score

label<-"(a) AHP score of the selected nodes"
library(EnvStats)
limit<-c(0,0.6)
par(mfrow = c(1,2), cex.lab=1.0,cex=0.9, mar=c(4,5,1,2))
plot(density(ahp_ahp), col = 'white',main="",yaxt="n", lty=1, lwd=3, xlim=c(0.05,0.6), xlab=label) # plots the result
lines(density(it_ahp), col = 'blue',main="",yaxt="n", lty=1, lwd=3, xlab="") # plots the results
lines(density(ahp_ahp),  col = 'red',main="",yaxt="n", lty=3, lwd=3, xlab="") # plots the results
legend("topright", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"), lty=c(3,1),pch=NA,
       ncol=1, cex=1,bty="n", lwd=3)
# label<-"(b) QoIT score of the selected nodes"
# plot(density(it_it), col = 'white',main="",yaxt="n", lty=1, lwd=3, xlim=c(0.6,1), xlab=label) # plots the result
# lines(density(ahp_it), col = 'blue',main="",yaxt="n", lty=1, lwd=3, xlab="") # plots the results
# lines(density(it_it),  col = 'red',main="",yaxt="n", lty=3, lwd=3, xlab="") # plots the results
# legend("topleft", legend=c("QoI-AHP","QoIT"),
#        col=c("red","blue"), lty=c(3,1),pch=NA,
#        ncol=1, cex=1,bty="n", lwd=3)
library(dplyr)
this<-pnp_static
s30_ahp<-this[this$users%in%30 & this$method%in%"ahp",]
s30ahp<-group_by(s30_ahp, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

s30_it<-this[this$users%in%30 & this$method%in%"it",]
s30it<-group_by(s30_it, run) %>%
  summarise(per=mean(Percentage), pri=mean(Priority),num=mean(Number))

per<-c(s30ahp$pri,s30it$pri)
per.label<-c(rep(1:2,each=20))
d<-NULL
d$label<-per.label
d$value<-per
d<-as.data.frame(d)
outstat<- boxplot(value~label,
                  data=d,
                  #at=c(1:2, 4:5),
                  main="",
                  ylab="Average overall priority of\nquality metrics met",
                  xlab="(b) Static scenario",
                  xaxt="n",
                  yaxt="n",
                  col=c(NA,NA), density=seq(30,30), lty= c(1, 1),
                  border=c("red","blue"),axes=TRUE,outline=FALSE
)
ngroups<- 2         # get number of groups
dval<- seq(20,20)            # density vals for each group
aval<- c( 0, 90)       # angle vals for each group
rect((1:4)-.4, outstat$stats[2,], (1:4)+.4, outstat$stats[4,],
     density=dval, angle=aval,lty=c(3,1),col = c("red","blue"))            # draw the rectangles
axis(side=2, las=2)
legend("topleft", legend=c("QoI-AHP","QoIT"),
       col=c("red","blue"),angle=c( 90, 0), 
       lty= c(3, 1), cex=0.9,bty="n",lwd=3)

##------------
