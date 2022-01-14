package com.mongol.utils;

/************************************
阴历及节日程序 2008 beta1
免费软件(Free Software) 你可以无限传播与反编译
该日历有三种外观样式,有从1900年至2049年间的所有阴历
个人爱好开发  作者:朱春 Email:npuxbd@163.com
Copyright @ 2008- All Rights Reserved
FileName:Simple_Calendar
**********************************/
import java.text.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.util.*;
import javax.swing.*;
public class Simple_Calendar extends JFrame implements ActionListener{



public static void main(String[] args) {
 
 SwingConsole.run(new Simple_Calendar(),525,525);
}



public Simple_Calendar() {
 setTitle(" 阴历及节日软件 2008 beta1");
 setIconImage(this.getToolkit().createImage("title.gif"));
 setResizable(false);
 calendar = Calendar.getInstance();
 today = calendar.get(Calendar.DAY_OF_MONTH);
 panel_Header=initializtion_Header();
 panel_Week =initializtion_Week();
 panel_Calendar=initializtion_Calendar();
  setLayout(null);
 setBounds(185,125,0,0);
 add(panel_Header);panel_Calendar.setBounds(0 ,0  ,525 , 45);
 add(panel_Week);     panel_Week.setBounds(0 ,45 ,525 , 35);
    add(panel_Calendar);panel_Calendar.setBounds(0 ,80 ,525 , 415) ;
    initializtion_Data(calendar);
   
 
 
}
   private  JPanel initializtion_Header(){//显示表头的panel
     
    JPanel panel = new JPanel();
    year_box  = new  JComboBox();
    month_box = new JComboBox();
    cross=new JRadioButton("c",false);cross.addActionListener(new LookAndFeel_Listener());
    system=new JRadioButton("s",false);system.addActionListener(new LookAndFeel_Listener());
    motif=new JRadioButton("m",false);motif.addActionListener(new LookAndFeel_Listener());
    feel_group= new ButtonGroup();
    show_help = new JButton("说 明");
    
    show_help.addActionListener(new Statement_Listener());
    
    feel_group.add(cross);feel_group.add(system);feel_group.add(motif);
    panel.setBorder(new EtchedBorder(5,Color.red,Color.BLUE));
    JLabel year_l = new JLabel("请您选择年份: ",JLabel.RIGHT);
    JLabel month_l = new JLabel("月份: ",JLabel.RIGHT);
    panel.setLayout(null);
    panel.setSize(525,45);




    for(int i = 1900 ;i < 2050 ; i++)
       year_box.addItem(""+i);
    for(int j = 1 ; j <= 12 ;j++)
       month_box.addItem(""+j) ;
  
    year_box.setSelectedIndex(calendar.get(Calendar.YEAR)-1900);
    month_box.setSelectedIndex(calendar.get(Calendar.MONTH));
    panel.add(year_l);      year_l.setBounds(0,10,95,25);
    panel.add(year_box);  year_box.setBounds(100,10,65,25);
    panel.add(month_l);    month_l.setBounds(160,10,45,25);
    panel.add(month_box);month_box.setBounds(210,10,45,25);
    JLabel look_feel = new JLabel("外观:",JLabel.RIGHT);
    panel.add(look_feel);look_feel.setBounds(290-38,10,38,25);
    panel.add(cross);       cross.setBounds(290,10,38,25);
    panel.add(system);     system.setBounds(325,10,32,25);
    panel.add(motif);       motif.setBounds(355,10,38,25);
    panel.add(show_help);  show_help.setBounds(400,10,65,25);
    show_help.setBorder(new EmptyBorder(0,0,0,0));
    show_help.setBackground(Color.getHSBColor(23,21,10));
    year_box.addActionListener(this);
    month_box.addActionListener(this);
    return panel;
   }
   private JPanel initializtion_Week(){//显示星期的panel
 JPanel panel = new JPanel();
 
 panel.setLayout(new GridLayout(1,7));
 
 String columnNames[]={"星期日","星期一","星期二","星期三",
                                 "星期四","星期五","星期六"};
       JLabel label =null;
       for(int i=0;i<7;i++){
        label = new JLabel(columnNames[i],JLabel.CENTER);
        if(i == 0 || i == 6)
          label.setForeground(Color.RED);
        label.setBorder(new LineBorder(Color.BLUE));
        panel.add(label);
       }
       return panel;
}
   private JPanel initializtion_Calendar(){//显示日期的panel
       JPanel panel = new JPanel();
    
       panel.setLayout(new GridLayout(6,7));
       for( int i = 0 ; i < 6 ; i++ ){
         for(int j = 0 ; j < 7 ; j++ ){
        label=new JLabel("",JLabel.CENTER);
        
        datas[i][j] = label;
        label.setBorder(new LineBorder(Color.BLUE));
        if(j==0 || j==6)
           label.setForeground(Color.RED); 
        datas[i][j].addMouseListener(new List_MouseListener());
        panel.add(label);
       }
    }
    return panel;
   }
  
   public  void clear_Data(){//清空内容的
    for(int i = 0 ; i < 6 ; i++ )
     for(int j = 0 ; j < 7 ; j++ ){
      datas[i][j].setText("");
      if(j==0 || j==6)
         datas[i][j].setForeground(Color.RED);
      else
         datas[i][j].setForeground(null);
     }
        
   }
   public  void initializtion_Data(Calendar calendar){//初始化函数
  ////////////////////////////////////////////////////
  ////////////////////////////////////////////////////////////
   /* 节日和纪念日
格式：起始年(yyyy)+月(mm)+日(dd)



0000表示起始年不明*/
HashMap<String,String>  sFestival =new  HashMap<String,String>();
//String []sFestival_={
sFestival.put("0101","  元旦");
sFestival.put("0214","情人节");
sFestival.put("0308","妇女节");
sFestival.put("0312","植树节");
sFestival.put("0401","愚人节");
sFestival.put("0501","劳动节");
sFestival.put("0504","青年节");
sFestival.put("0601","儿童节");
sFestival.put("0701","建党节");
sFestival.put("0801","建军节");
sFestival.put("0910","教师节");
sFestival.put("1001","国庆节");
sFestival.put("1031","万圣节");
sFestival.put("1112","孙中山诞辰");
sFestival.put("1225","圣诞节");
sFestival.put("1226","毛泽东诞辰");
//};
//某月第几个星期几
//起始年(4位)+月(2位)+第几个(1位)+星期几(1位)
HashMap<String,String>  wFestival =new  HashMap<String,String>();
//String []wFestival={
wFestival.put("0520","母亲节");
wFestival.put("0630","父亲节");
wFestival.put("1144","感恩节");
//};
//农历 99表示月最后一天
HashMap<String,String>  lFestival =new  HashMap<String,String>();
//String []lFestival={
lFestival.put("0101","春 节");
lFestival.put("0102","大年初二");
lFestival.put("0103","大年初三");
lFestival.put("0115","元宵节");
lFestival.put("0505","端午节");
lFestival.put("0707","七 夕");
lFestival.put("0815","中秋节");
lFestival.put("0909","重阳节");
lFestival.put("1208","腊八节");
lFestival.put("1299","除 夕");
//};
  
/////////////////////////////////////////////////////////////
  
  //////////////////////////////////////////////////// 
     this.calendar = calendar;
     today = calendar.get(Calendar.DAY_OF_MONTH);
     int month = calendar.get(Calendar.MONTH);
     int weekindexDay;
  int weekindexMonth;
 
  calendar.set(Calendar.DATE,1);
 
 while(calendar.get(Calendar.MONTH)==month)
 {   weekindexMonth=calendar.get(Calendar.WEEK_OF_MONTH)-1;
     weekindexDay=calendar.get(Calendar.DAY_OF_WEEK)-1;
     int day=calendar.get(Calendar.DAY_OF_MONTH);
     ///////////////////////////////////////////////
     String today_,month_;
     today_ =day < 10?  "0" + day:"" + day;
     month_ =month<10?  "0" +(month+1):""+(month+1);
     Lunar lunar = new Lunar(calendar);
     String lunar_= lunar.toString();
     ///////////////////////////////////////////
     if( null != sFestival.get(month_+today_))
          lunar_="<font color=red>"+sFestival.get(month_+today_);
     ///////
     String wFestival_=month_+(weekindexMonth)+(weekindexDay);
    
     if( null != wFestival.get(wFestival_)){
      lunar_="<font color=red>"+wFestival.get(wFestival_);
      System.out.println(wFestival_);
     }
        
    
    
    
     if( null != lFestival.get(lunar.numeric_md()))
         lunar_="<font color=red>"+lFestival.get(lunar.numeric_md());
    
    
        
     //计算除夕
     Calendar temp_calendar = Calendar.getInstance();
     temp_calendar.set(calendar.get(Calendar.YEAR),month,day+1);
    
     //temp_calendar.add(Calendar.DAY_OF_MONTH,1);
     Lunar temp_lunar = new Lunar(temp_calendar);
     String temp_str = temp_lunar.numeric_md();
      if(temp_str.equals("0101"))
         lunar_="<font color=red>"+lFestival.get("1299");
     ///计算除夕结束
     //////////////////////////////////////////
     String day_str;
     if(day<10) day_str="<html><center><font size=6>"+today_;
     else       day_str="<html><center><font size=6>"+today_;
    
     day_str+="</font><br>"+lunar_;
  
     if(day==today)
        datas[weekindexMonth][weekindexDay].setForeground(Color.GREEN);
     datas[weekindexMonth][weekindexDay].setText(day_str);
     calendar.add(Calendar.DATE,1);
 } 
    
    
    
/////////////////////////////////////////////////    
   }
   public  void actionPerformed(ActionEvent e){//日期和年份的选择更新
    int year = Integer.parseInt(year_box.getSelectedItem().toString()) ;
    int month= Integer.parseInt(month_box.getSelectedItem().toString())-1 ;
    calendar.set(year,month,today);
    clear_Data();
       initializtion_Data(calendar);
   }
   class LookAndFeel_Listener implements ActionListener{//选择外观的样式
public void actionPerformed(ActionEvent ev){
 JRadioButton o = (JRadioButton)ev.getSource();
 String str=o.getText();
 if("c" == str){
  try {UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
           } catch(Exception e) { e.printStackTrace(); }
 }else if("s" == str){
  try{UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch(Exception e) {e.printStackTrace();}
 }else if("m" == str){
  try{UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MotifLookAndFeel");}
           catch(Exception e) {e.printStackTrace(); }
 }
 SwingUtilities.updateComponentTreeUI(Simple_Calendar.this);
  }

   }
   class Statement_Listener implements ActionListener{//显示声明信息
    JDialog dialog=null;
    public void actionPerformed(ActionEvent ev){
       String statment = "<html>阴历及节日程序 2008 beta1";
        statment +="<br>免费软件(Free Software) 你可以无限传播与反编译.";
        statment +="<br>该日历有三种外观样式,有从1900年至2049年间的所有阴历.";
        statment +="<br>个人爱好开发  作者:朱春 Email:npuxbd@163.com";
        statment +="<br>Copyright @ 2008- All Rights Reserved";
       dialog=new  JDialog(Simple_Calendar.this, "阴历及节日软件 2008 beta1",true);
             dialog.setLayout(null);
             dialog.setBounds(285,215,365,185);
             JLabel label_s = new JLabel(statment);//label_s.setBackground(Color.RED);
             JButton button=new JButton("确 定");
             button.addActionListener(new ActionListener(){
              public void actionPerformed(ActionEvent evet){
                 dialog.setVisible(false);
              }
             });
             dialog.add(label_s);label_s.setBounds(20,0,365,100);
             dialog.add(button);  button.setBounds(145,110,65,25);
             dialog.setVisible(true);
      }
    
   }
   class List_MouseListener implements MouseListener{//鼠标移入时显示的信息
    JLabel labe=null;
    String weeks[]={"星期日","星期一","星期二","星期三",
                                 "星期四","星期五","星期六"};
                                



       public String constellation(Calendar cal){
 
               String [][]con ={
                    {"水瓶座","0122","0221"},
                    {"双鱼座","0222","0321"},
                    {"白羊座","0322","0420"},
                    {"金牛座","0421","0521"},
                    {"双子座","0522","0621"},
                    {"巨蟹座","0621","0721"},
                    {"狮子座","0722","0821"},
                    {"处女座","0822","0921"}, 
                    {"天秤座","0922","1021"},
                    {"天蝎座","1022","1121"},
                    {"射手座","1122","1221"},
                    {"摩羯座","1222","0121"}};
               int month=cal.get(Calendar.MONTH)+1;
               int today=cal.get(Calendar.DAY_OF_MONTH);
               String month_str=month<10?"0"+month:""+month;
               String today_str=today<10?"0"+today:""+today;
               String str=month_str+today_str;
               for( int i = 0 ; i < con.length-1 ; i++ )
               {
                    if(Integer.parseInt(str) >= Integer.parseInt(con[i][1]) && Integer.parseInt(str) <= Integer.parseInt(con[i][2]))
                       return con[i][0]; 
                
               }
               if((Integer.parseInt(str) >= Integer.parseInt(con[11][1]) && Integer.parseInt(str) < 1232) ||  Integer.parseInt(str) <=Integer.parseInt( con[11][2]) )
                  return  con[11][0];
      
              return "error!";
       }
      
    public void mouseClicked(MouseEvent e) {//鼠标按键在组件上单击（按下并释放）时调用。
       
       }    
       public void mouseEntered(MouseEvent e) {//鼠标进入到组件上时调用。
            labe=(JLabel)e.getSource();
            String lab =labe.getText();
            if(lab != ""){
             labe.setBackground(Color.red);
             String day=lab.substring(lab.indexOf("size=6>")+7,lab.indexOf("</font>"));
               //String lun=lab.substring(lab.indexOf("<br>")+4);
               String message = "<html><body><center>公元 "+year_box.getSelectedItem()+"年"+
                                month_box.getSelectedItem()+"月"+Integer.parseInt(day)+"日";
               calendar.set(Integer.parseInt(year_box.getSelectedItem().toString()),
                            Integer.parseInt(month_box.getSelectedItem().toString())-1,
                            Integer.parseInt(day));
               Lunar lunar=new Lunar(calendar);
               message+="<br><font color=red>"+weeks[(calendar.get(calendar.DAY_OF_WEEK)-1)];
               message+="&nbsp;&nbsp;&nbsp;&nbsp;"+constellation(calendar)+"</font><br>农历 ";
               message+=lunar.get_month()+"月"+lunar.get_Big_Or_Small()+"&nbsp;&nbsp;&nbsp;&nbsp;"+lunar.get_date()+"日";
               //message+=lunar.get_JQ();
               labe.setToolTipText(message);
               labe.setBackground(Color.BLUE);
               labe.setOpaque(true);
              // System.out.println(day+":"+lun);
            }
           
       } 
       public void mouseExited(MouseEvent e) {//  鼠标离开组件时调用。
                 
       
                       labe.setBackground(null);
                       labe.setOpaque(true);
       }
       public void mousePressed(MouseEvent e) {//  鼠标按键在组件上按下时调用。
      
       }
       public void mouseReleased(MouseEvent e) {//  鼠标按钮在组件上释放时调用。
       }



   }
   /////////////////////////////////////////////////////////
   private JLabel datas[][]=new JLabel[6][7];//显示一月的日期
   private JLabel temp_label=null;
   private  JLabel label=null;
   private JPanel panel_Header,panel_Week,panel_Calendar;
   private Calendar calendar=null;
   private  JComboBox  year_box = null ;
   private  JComboBox month_box = null ;
   private int today ;
   private JRadioButton cross=null,system=null,motif=null;
   private ButtonGroup feel_group=null;
   private JButton show_help =null;
  
    
}







/*
*以下是阴历对象；
* 是从网络中得来的；
*/
 class  Lunar  {
    private   int  year;
    private   int  month;
    private   int  day;
    private   boolean  leap;
    final   static  String chineseNumber[]  =   { "一","二","三","四","五","六","七","八","九","十","十一","十二"};
    final   static  String Big_Or_Small[]  =    { "大","小","大","小","大","小","大","大","小","大","小"  ,"大"};
    private String[] LunarHolDayName =
           {
               "小寒", "大寒", "立春", "雨水",
               "惊蛰", "春分", "清明", "谷雨",
               "立夏", "小满", "芒种", "夏至",
               "小暑", "大暑", "立秋", "处暑",
               "白露", "秋分", "寒露", "霜降",
               "立冬", "小雪", "大雪", "冬至"};



   
    static  SimpleDateFormat chineseDateFormat  =   new  SimpleDateFormat( " yyyy年MM月dd日 " );
    final   static   long [] lunarInfo  =   new   long []
    { 0x04bd8 ,  0x04ae0 ,  0x0a570 ,  0x054d5 ,  0x0d260 ,  0x0d950 ,  0x16554 ,  0x056a0 ,  0x09ad0 ,  0x055d2 ,
     0x04ae0 ,  0x0a5b6 ,  0x0a4d0 ,  0x0d250 ,  0x1d255 ,  0x0b540 ,  0x0d6a0 ,  0x0ada2 ,  0x095b0 ,  0x14977 ,
     0x04970 ,  0x0a4b0 ,  0x0b4b5 ,  0x06a50 ,  0x06d40 ,  0x1ab54 ,  0x02b60 ,  0x09570 ,  0x052f2 ,  0x04970 ,
     0x06566 ,  0x0d4a0 ,  0x0ea50 ,  0x06e95 ,  0x05ad0 ,  0x02b60 ,  0x186e3 ,  0x092e0 ,  0x1c8d7 ,  0x0c950 ,
     0x0d4a0 ,  0x1d8a6 ,  0x0b550 ,  0x056a0 ,  0x1a5b4 ,  0x025d0 ,  0x092d0 ,  0x0d2b2 ,  0x0a950 ,  0x0b557 ,
     0x06ca0 ,  0x0b550 ,  0x15355 ,  0x04da0 ,  0x0a5d0 ,  0x14573 ,  0x052d0 ,  0x0a9a8 ,  0x0e950 ,  0x06aa0 ,
     0x0aea6 ,  0x0ab50 ,  0x04b60 ,  0x0aae4 ,  0x0a570 ,  0x05260 ,  0x0f263 ,  0x0d950 ,  0x05b57 ,  0x056a0 ,
     0x096d0 ,  0x04dd5 ,  0x04ad0 ,  0x0a4d0 ,  0x0d4d4 ,  0x0d250 ,  0x0d558 ,  0x0b540 ,  0x0b5a0 ,  0x195a6 ,
     0x095b0 ,  0x049b0 ,  0x0a974 ,  0x0a4b0 ,  0x0b27a ,  0x06a50 ,  0x06d40 ,  0x0af46 ,  0x0ab60 ,  0x09570 ,
     0x04af5 ,  0x04970 ,  0x064b0 ,  0x074a3 ,  0x0ea50 ,  0x06b58 ,  0x055c0 ,  0x0ab60 ,  0x096d5 ,  0x092e0 ,
     0x0c960 ,  0x0d954 ,  0x0d4a0 ,  0x0da50 ,  0x07552 ,  0x056a0 ,  0x0abb7 ,  0x025d0 ,  0x092d0 ,  0x0cab5 ,
     0x0a950 ,  0x0b4a0 ,  0x0baa4 ,  0x0ad50 ,  0x055d9 ,  0x04ba0 ,  0x0a5b0 ,  0x15176 ,  0x052b0 ,  0x0a930 ,
     0x07954 ,  0x06aa0 ,  0x0ad50 ,  0x05b52 ,  0x04b60 ,  0x0a6e6 ,  0x0a4e0 ,  0x0d260 ,  0x0ea65 ,  0x0d530 ,
     0x05aa0 ,  0x076a3 ,  0x096d0 ,  0x04bd7 ,  0x04ad0 ,  0x0a4d0 ,  0x1d0b6 ,  0x0d250 ,  0x0d520 ,  0x0dd45 ,
     0x0b5a0 ,  0x056d0 ,  0x055b2 ,  0x049b0 ,  0x0a577 ,  0x0a4b0 ,  0x0aa50 ,  0x1b255 ,  0x06d20 ,  0x0ada0 } ;
    
         // ====== 传回农历 y年的总天数
    final   private   static   int  yearDays( int  y)  {
        int  i, sum  =   348 ;
        for  (i  =   0x8000 ; i  >   0x8 ; i  >>=   1 )  {
            if  ((lunarInfo[y  -   1900 ]  &  i)  !=   0 ) sum  +=   1 ;
       }
        return  (sum  +  leapDays(y));
   }



    // ====== 传回农历 y年闰月的天数
    final   private   static   int  leapDays( int  y)  {
        if  (leapMonth(y)  !=   0 )  {
            if  ((lunarInfo[y  -   1900 ]  &   0x10000 )  !=   0 )
                return   30 ;
            else
                return   29 ;
       }   else
            return   0 ;
   }



    // ====== 传回农历 y年闰哪个月 1-12 , 没闰传回 0
    final   private   static   int  leapMonth( int  y)  {
        return  ( int ) (lunarInfo[y  -   1900 ]  &   0xf );
   }



    // ====== 传回农历 y年m月的总天数
    final   private   static   int  monthDays( int  y,  int  m)  {
        if  ((lunarInfo[y  -   1900 ]  &  ( 0x10000   >>  m))  ==   0 )
            return   29 ;
        else
            return   30 ;
   }



    // ====== 传回农历 y年的生肖
    final   public  String animalsYear()  {
        final  String[] Animals  =   new  String[] { "鼠","牛","虎","兔","龙","蛇","马","羊","猴","鸡","狗","猪"};
       return  Animals[(year  -   4 )  %   12 ];
   }



    // ====== 传入 月日的offset 传回干支, 0=甲子
    final   private   static  String cyclicalm( int  num)  {
        final  String[] Gan  =   new  String[] { "甲","乙","丙","丁","戊","己","庚","辛","壬","癸"};
        final  String[] Zhi  =   new  String[] { "子","丑","寅","卯","辰","巳","午","未","申","酉","戌","亥"};
        return  (Gan[num  %   10 ]  +  Zhi[num  %   12 ]);
   }



    // ====== 传入 offset 传回干支, 0=甲子
    final   public  String cyclical()  {
        int  num  =  year  -   1900   +   36 ;
        return  (cyclicalm(num));
   }



    /** */ /**
    * 传出y年m月d日对应的农历.
    * yearCyl3:农历年与1864的相差数              ?
    * monCyl4:从1900年1月31日以来,闰月数
    * dayCyl5:与1900年1月31日相差的天数,再加40      ?
    *  @param  cal
    *  @return 
     */
    public  Lunar(Calendar cal)  {
     //cal.add(cal.get(Calendar.DAY_OF_MONTH),1);
       @SuppressWarnings( " unused " )  int  yearCyl, monCyl, dayCyl;
        int  leapMonth  =   0 ;
       Date baseDate  =   null ;
        try   {
           baseDate  =  chineseDateFormat.parse( " 1900年1月31日 " );
       }   catch  (ParseException e)  {
           e.printStackTrace();   // To change body of catch statement use Options | File Templates.
       }



        // 求出和1900年1月31日相差的天数
        int  offset  =  ( int ) ((cal.getTime().getTime()  -  baseDate.getTime())  /   86400000L );
       dayCyl  =  offset  +   40 ;
       monCyl  =   14 ;



        // 用offset减去每农历年的天数
        //  计算当天是农历第几天
        // i最终结果是农历的年份
        // offset是当年的第几天
        int  iYear, daysOfYear  =   0 ;
        for  (iYear  =   1900 ; iYear  <   2050   &&  offset  >   0 ; iYear ++ )  {
           daysOfYear  =  yearDays(iYear);
           offset  -=  daysOfYear;
           monCyl  +=   12 ;
       }
        if  (offset  <   0 )  {
           offset  +=  daysOfYear;
           iYear-- ;
           monCyl  -=   12 ;
       }
        // 农历年份
       year  =  iYear;



       yearCyl  =  iYear  -   1864 ;
       leapMonth  =  leapMonth(iYear);  // 闰哪个月,1-12
       leap  =   false ;



        // 用当年的天数offset,逐个减去每月（农历）的天数，求出当天是本月的第几天
        int  iMonth, daysOfMonth  =   0 ;
        for  (iMonth  =   1 ; iMonth  <   13   &&  offset  >   0 ; iMonth ++ )  {
            // 闰月
            if  (leapMonth  >   0   &&  iMonth  ==  (leapMonth  +   1 )  &&   ! leap)  {
                -- iMonth;
               leap  =   true ;
               daysOfMonth  =  leapDays(year);
           }   else
               daysOfMonth  =  monthDays(year, iMonth);



           offset  -=  daysOfMonth;
            // 解除闰月
            if  (leap  &&  iMonth  ==  (leapMonth  +   1 )) leap  =   false ;
            if  ( ! leap) monCyl ++ ;
       }
        // offset为0时，并且刚才计算的月份是闰月，要校正
        if  (offset  ==   0   &&  leapMonth  >   0   &&  iMonth  ==  leapMonth  +   1 )  {
            if  (leap)  {
               leap  =   false ;
           }   else   {
               leap  =   true ;
                -- iMonth;
                -- monCyl;
           }
       }
        // offset小于0时，也要校正
        if  (offset  <   0 )  {
           offset  +=  daysOfMonth;
            -- iMonth;
            -- monCyl;
       }
       month  =  iMonth;
       day  =  offset  +   1 ;
   }



    public   static  String getChinaDayString( int  day)  {
       String chineseTen[]  =   { "初" ,  "十" ,  "廿" ,  "卅" } ;
        int  n  =  day  %   10   ==   0   ?   9  : day  %   10   -   1 ;
        if  (day  >   30 )
            return   "" ;
        if  (day  ==   10 )
            return   "初十" ;
        else
            return  chineseTen[day  /   10 ]  +  chineseNumber[n];
   }



   public  String toString()  {
        return  /*cyclical() +   "年"   + */ (leap  ?   "闰"  :  "" )  +  chineseNumber[month  -   1 ]  +   "月"   +  getChinaDayString(day);
   }
   public String numeric_md(){//返回阿拉伯数字的阴历日期
    String temp_day;
    String temp_mon;
    temp_mon=month<10?"0"+month:""+month;
    temp_day=day < 10?"0"+ day:""+ day;
    
    return temp_mon+temp_day;
   }
   public String get_month(){//返回阴历的月份
    return chineseNumber[month  -   1 ];
   }
   public String get_date(){//返回阴历的天
    return getChinaDayString(day);
   }
   public String get_Big_Or_Small(){//返回的月份的大或小
    return Big_Or_Small[month  -   1 ];
   }
  
  
}
 class SwingConsole {//提供安全线程机制
public static void run(final JFrame f,final int width,final int height){
 SwingUtilities.invokeLater(new Runnable(){
  public void run(){
  
   f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
   f.setSize(width,height);
   f.setVisible(true);
  }
 });
}
}


