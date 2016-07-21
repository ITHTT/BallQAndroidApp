package com.tysci.ballq.modles;

/**
 * Created by Administrator on 2016-07-20 0020.
 *
 * @see com.tysci.ballq.views.adapters.UserAttentionListAdapter
 */
public class UserAttentionListEntity
{
    private int mtype = 1;
    private Data data;

    public int getMtype()
    {
        return mtype;
    }

    public void setMtype(int mtype)
    {
        this.mtype = mtype;
    }

    public Data getData()
    {
        return data;
    }

    public void setData(Data data)
    {
        this.data = data;
    }

    public final static class Data
    {
        private int atid;
        private String atlogo;
        private String atname;
        private String atscore;

        private int btyc;

        private int champion_id;
        private String choice;
        private int confidence;
        private int comcount;
        private String cont;
        private String cover;
        private String ctime;

        private int eid;
        private int etype;

        private int fcount;
        private int fid;
        private String fname;
        private String first_image;

        private int group_id;

        private int htid;
        private String htlogo;
        private String htname;
        private String htscore;

        private int id;
        private int is_champion;
        private int isc;
        private int isf;
        private int isv;
        private int ist;

        private int like_count;

        private int mtcount;
        private String mtime;
        private int mstatus;
        private String mtstatus;

        private String otype;

        private String pt;

        private String odata;

        private int ram;
        private int reading_count;
        private int richtext_type;
        private String ror;
        private int rtype;

        private int sam;
        private int settled;
        private int sorder;
        private int status;

        private int tbtyc;
        private int tipcount;
        private String title;
        private String title1;
        private String title2;
        private String tourname;
        private int tournament_id;

        private int uid;

        private String vid;

        private String wins;

        public String getAtlogo()
        {
            return atlogo;
        }

        public void setAtlogo(String atlogo)
        {
            this.atlogo = atlogo;
        }

        public String getHtlogo()
        {
            return htlogo;
        }

        public void setHtlogo(String htlogo)
        {
            this.htlogo = htlogo;
        }

        public int getReading_count()
        {
            return reading_count;
        }

        public void setReading_count(int reading_count)
        {
            this.reading_count = reading_count;
        }

        public int getAtid()
        {
            return atid;
        }

        public void setAtid(int atid)
        {
            this.atid = atid;
        }

        public String getAtname()
        {
            return atname;
        }

        public void setAtname(String atname)
        {
            this.atname = atname;
        }

        public String getAtscore()
        {
            return atscore;
        }

        public void setAtscore(String atscore)
        {
            this.atscore = atscore;
        }

        public int getBtyc()
        {
            return btyc;
        }

        public void setBtyc(int btyc)
        {
            this.btyc = btyc;
        }

        public int getChampion_id()
        {
            return champion_id;
        }

        public void setChampion_id(int champion_id)
        {
            this.champion_id = champion_id;
        }

        public String getChoice()
        {
            return choice;
        }

        public void setChoice(String choice)
        {
            this.choice = choice;
        }

        public int getConfidence()
        {
            return confidence;
        }

        public void setConfidence(int confidence)
        {
            this.confidence = confidence;
        }

        public int getComcount()
        {
            return comcount;
        }

        public void setComcount(int comcount)
        {
            this.comcount = comcount;
        }

        public String getCont()
        {
            return cont;
        }

        public void setCont(String cont)
        {
            this.cont = cont;
        }

        public String getCtime()
        {
            return ctime;
        }

        public void setCtime(String ctime)
        {
            this.ctime = ctime;
        }

        public int getEid()
        {
            return eid;
        }

        public void setEid(int eid)
        {
            this.eid = eid;
        }

        public int getEtype()
        {
            return etype;
        }

        public void setEtype(int etype)
        {
            this.etype = etype;
        }

        public int getFcount()
        {
            return fcount;
        }

        public String getCover()
        {
            return cover;
        }

        public void setCover(String cover)
        {
            this.cover = cover;
        }

        public void setFcount(int fcount)
        {
            this.fcount = fcount;
        }

        public String getFname()
        {
            return fname;
        }

        public void setFname(String fname)
        {
            this.fname = fname;
        }

        public int getHtid()
        {
            return htid;
        }

        public void setHtid(int htid)
        {
            this.htid = htid;
        }

        public String getHtname()
        {
            return htname;
        }

        public void setHtname(String htname)
        {
            this.htname = htname;
        }

        public String getHtscore()
        {
            return htscore;
        }

        public void setHtscore(String htscore)
        {
            this.htscore = htscore;
        }

        public int getId()
        {
            return id;
        }

        public void setId(int id)
        {
            this.id = id;
        }

        public int getIs_champion()
        {
            return is_champion;
        }

        public void setIs_champion(int is_champion)
        {
            this.is_champion = is_champion;
        }

        public int getIst()
        {
            return ist;
        }

        public void setIst(int ist)
        {
            this.ist = ist;
        }

        public int getLike_count()
        {
            return like_count;
        }

        public void setLike_count(int like_count)
        {
            this.like_count = like_count;
        }

        public int getMtcount()
        {
            return mtcount;
        }

        public void setMtcount(int mtcount)
        {
            this.mtcount = mtcount;
        }

        public String getMtime()
        {
            return mtime;
        }

        public void setMtime(String mtime)
        {
            this.mtime = mtime;
        }

        public int getMstatus()
        {
            return mstatus;
        }

        public void setMstatus(int mstatus)
        {
            this.mstatus = mstatus;
        }

        public String getMtstatus()
        {
            return mtstatus;
        }

        public void setMtstatus(String mtstatus)
        {
            this.mtstatus = mtstatus;
        }

        public String getOtype()
        {
            return otype;
        }

        public void setOtype(String otype)
        {
            this.otype = otype;
        }

        public String getPt()
        {
            return pt;
        }

        public void setPt(String pt)
        {
            this.pt = pt;
        }

        public String getOdata()
        {
            return odata;
        }

        public void setOdata(String odata)
        {
            this.odata = odata;
        }

        public int getRam()
        {
            return ram;
        }

        public void setRam(int ram)
        {
            this.ram = ram;
        }

        public String getRor()
        {
            return ror;
        }

        public void setRor(String ror)
        {
            this.ror = ror;
        }

        public int getSam()
        {
            return sam;
        }

        public void setSam(int sam)
        {
            this.sam = sam;
        }

        public int getSettled()
        {
            return settled;
        }

        public void setSettled(int settled)
        {
            this.settled = settled;
        }

        public int getStatus()
        {
            return status;
        }

        public void setStatus(int status)
        {
            this.status = status;
        }

        public int getTbtyc()
        {
            return tbtyc;
        }

        public void setTbtyc(int tbtyc)
        {
            this.tbtyc = tbtyc;
        }

        public int getTipcount()
        {
            return tipcount;
        }

        public void setTipcount(int tipcount)
        {
            this.tipcount = tipcount;
        }

        public String getTitle()
        {
            return title;
        }

        public void setTitle(String title)
        {
            this.title = title;
        }

        public String getTourname()
        {
            return tourname;
        }

        public void setTourname(String tourname)
        {
            this.tourname = tourname;
        }

        public int getTournament_id()
        {
            return tournament_id;
        }

        public void setTournament_id(int tournament_id)
        {
            this.tournament_id = tournament_id;
        }

        public int getUid()
        {
            return uid;
        }

        public void setUid(int uid)
        {
            this.uid = uid;
        }

        public String getVid()
        {
            return vid;
        }

        public void setVid(String vid)
        {
            this.vid = vid;
        }

        public int getIsv()
        {
            return isv;
        }

        public void setIsv(int isv)
        {
            this.isv = isv;
        }
    }
}
