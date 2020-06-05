library("stringr")
##### STEP 1************************************************************************************
#Waccuracy = 0.4, Wreliability = 0.3, Wcompleteness = 0.2, Wtimeliness = 0.1
path="/Users/mahrukh/Downloads/QoIT/Sameweights/lessvsmore/Mobile"
m.files<-Mobile_QOIT(path)
write.table(m.files$a, "Time_mobile.csv",sep=",")
write.table(m.files$b, "Nmetric_mobile.csv",sep=",")
write.table(m.files$c, "Pnp_mobile.csv",sep=",")
write.table(m.files$d, "Score_mobile.csv",sep=",")
# For Static ----------------------------
#path="/QoIT/Sameweights/Static"
#path="/QoIT/Sameweights/SamevsDiff/Diff"
path="/QoIT/Sameweights/ALLScore"
s.files<-Static_QOIT(path)
write.table(s.files$a, "Time_static.csv",sep=",")
write.table(s.files$b, "Nmetric_static.csv",sep=",")
write.table(s.files$c, "Pnp_static.csv",sep=",")
write.table(s.files$d, "Score_static.csv",sep=",")
write.table(s.files$e, "AllScore_static.csv",sep=",")
