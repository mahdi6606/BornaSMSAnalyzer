package ir.asemaneh.bornasmsanalyzer;

public class BaleResponseSendMessage {

    public boolean ok;
    public Result result;

    public class Result{
        public int message_id;
        public From from;
        public int date;
        public Chat chat;
        public String text;
    }


    public class Chat{
        public long id;
        public String type;
        public Object photo;
    }

    public class From{
        public int id;
        public boolean is_bot;
        public String first_name;
        public String last_name;
        public String username;
    }



}
