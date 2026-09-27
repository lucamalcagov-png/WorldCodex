package com.example.worldcodex;

import android.app.*;import android.os.*;import android.content.*;import android.graphics.Color;import android.graphics.Typeface;import android.graphics.drawable.GradientDrawable;import android.view.*;import android.view.inputmethod.InputMethodManager;import android.widget.*;import org.json.*;import java.util.*;

public class MainActivity extends Activity {
  int GOLD=Color.rgb(216,181,106), INK=Color.rgb(20,19,26), PANEL=Color.rgb(33,30,43), PANEL2=Color.rgb(42,38,54), MIST=Color.rgb(238,233,221), MUTED=Color.rgb(170,163,183), LINE=Color.rgb(59,53,73), ACCENT=Color.rgb(143,120,199);
  LinearLayout root, content, drawer; TextView title, subtitle; SharedPreferences db;
  String current="Главная";
  String[] sections={"Главная","Персонажи","Локации","Фракции","Расы","Государства","Сюжет","Хронология","Магия","Предметы"};
  String[] icons={"⌂","♙","⌖","⚔","✧","♜","✦","◷","✺","◆"};
  String[] keys={"characters","locations","factions","races","states","plot","timeline","magic","items"};

  @Override public void onCreate(Bundle b){super.onCreate(b); db=getSharedPreferences("world",0); build(); showHome();}
  TextView tv(String s,int sp,int color){ TextView v=new TextView(this); v.setText(s);v.setTextSize(sp);v.setTextColor(color);v.setGravity(Gravity.CENTER_VERTICAL);v.setFontFeatureSettings("kern"); return v; }
  GradientDrawable bg(int color,float r){GradientDrawable g=new GradientDrawable();g.setColor(color);g.setCornerRadius(r);return g;}
  int dp(float x){return (int)(x*getResources().getDisplayMetrics().density+.5f);}
  TextView button(String text){TextView v=tv(text,15,MIST);v.setPadding(dp(16),0,dp(16),0);v.setBackground(bg(PANEL2,dp(12)));return v;}
  void build(){
    root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setBackgroundColor(INK);setContentView(root);
    LinearLayout top=new LinearLayout(this);top.setGravity(Gravity.CENTER_VERTICAL);top.setPadding(dp(16),dp(10),dp(12),dp(8));
    TextView menu=tv("☰",26,GOLD);top.addView(menu,new LinearLayout.LayoutParams(dp(44),dp(48))); menu.setOnClickListener(v->toggleDrawer());
    LinearLayout heads=new LinearLayout(this);heads.setOrientation(LinearLayout.VERTICAL);title=tv("World Codex",20,MIST);title.setTypeface(Typeface.DEFAULT,Typeface.BOLD);subtitle=tv("ЭНЦИКЛОПЕДИЯ ВСЕЛЕННОЙ",10,GOLD);heads.addView(title);heads.addView(subtitle);top.addView(heads,new LinearLayout.LayoutParams(0,dp(48),1));
    TextView search=tv("⌕",26,MIST);top.addView(search,new LinearLayout.LayoutParams(dp(44),dp(48))); search.setOnClickListener(v->showSearch());
    root.addView(top); root.addView(line(),new LinearLayout.LayoutParams(-1,dp(1))); FrameLayout frame=new FrameLayout(this);root.addView(frame,new LinearLayout.LayoutParams(-1,0,1));
    content=new LinearLayout(this);content.setOrientation(LinearLayout.VERTICAL);content.setPadding(dp(16),dp(12),dp(16),dp(24));ScrollView sv=new ScrollView(this);sv.addView(content);frame.addView(sv,new FrameLayout.LayoutParams(-1,-1));
    drawer=new LinearLayout(this);drawer.setOrientation(LinearLayout.VERTICAL);drawer.setPadding(dp(18),dp(18),dp(18),dp(18));drawer.setBackgroundColor(Color.rgb(25,23,33));FrameLayout.LayoutParams dl=new FrameLayout.LayoutParams(dp(310),-1);dl.gravity=Gravity.START;dl.leftMargin=-dp(310);frame.addView(drawer,dl);
    TextView dtitle=tv("✦  МОЯ ВСЕЛЕННАЯ",18,GOLD);dtitle.setTypeface(Typeface.DEFAULT,Typeface.BOLD);drawer.addView(dtitle,new LinearLayout.LayoutParams(-1,dp(64)));drawer.addView(line());
    for(int i=0;i<sections.length;i++){ final int n=i; LinearLayout row=new LinearLayout(this);row.setGravity(Gravity.CENTER_VERTICAL);row.setPadding(dp(10),0,dp(8),0); TextView ic=tv(icons[i],22,GOLD);row.addView(ic,new LinearLayout.LayoutParams(dp(40),dp(52)));TextView tx=tv(sections[i],15,MIST);row.addView(tx,new LinearLayout.LayoutParams(0,dp(52),1));row.setOnClickListener(v->{toggleDrawer(); if(n==0)showHome();else showSection(n-1);});drawer.addView(row);}
  }
  View line(){View v=new View(this);v.setBackgroundColor(LINE);return v;}
  void toggleDrawer(){FrameLayout.LayoutParams p=(FrameLayout.LayoutParams)drawer.getLayoutParams();p.leftMargin=p.leftMargin<0?0:-dp(310);drawer.setLayoutParams(p);}
  void header(String t,String st){title.setText(t);subtitle.setText(st.toUpperCase());content.removeAllViews();}
  TextView label(String s){TextView v=tv(s,12,MUTED);v.setPadding(0,dp(12),0,dp(6));return v;}
  TextView card(String name,String desc,String meta){LinearLayout c=new LinearLayout(this);c.setOrientation(LinearLayout.VERTICAL);c.setPadding(dp(16),dp(14),dp(16),dp(14));c.setBackground(bg(PANEL,dp(16)));TextView a=tv(name,17,MIST);a.setTypeface(Typeface.DEFAULT,Typeface.BOLD);c.addView(a);if(desc!=null&&!desc.isEmpty()){TextView b=tv(desc,13,MUTED);b.setPadding(0,dp(5),0,0);c.addView(b);}if(meta!=null){TextView m=tv(meta,11,GOLD);m.setPadding(0,dp(8),0,0);c.addView(m);}c.setOnClickListener(v->editDialog(name,desc==null?"":desc));LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(-1,-2);lp.setMargins(0,0,0,dp(10));content.addView(c,lp);return c.getChildAt(0) instanceof TextView?(TextView)c.getChildAt(0):a;}
  void showHome(){header("World Codex","Энциклопедия вселенной");
    TextView hero=tv("Твоя вселенная начинается здесь",25,MIST);hero.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(hero,new LinearLayout.LayoutParams(-1,dp(70)));
    TextView p=tv("Собирай персонажей, места, историю и тайны мира в одном месте.",14,MUTED);content.addView(p,new LinearLayout.LayoutParams(-1,dp(48)));
    LinearLayout stats=new LinearLayout(this);stats.setPadding(0,dp(10),0,dp(18));String[] names={"Персонажи","Локации","Фракции","Сюжет"};String[] ks={"characters","locations","factions","plot"};for(int i=0;i<4;i++){LinearLayout box=new LinearLayout(this);box.setOrientation(LinearLayout.VERTICAL);box.setPadding(dp(12),dp(10),dp(12),dp(8));box.setBackground(bg(PANEL,dp(14)));TextView num=tv(""+count(ks[i]),22,GOLD);num.setTypeface(Typeface.DEFAULT,Typeface.BOLD);TextView nm=tv(names[i],11,MUTED);box.addView(num);box.addView(nm);LinearLayout.LayoutParams bp=new LinearLayout.LayoutParams(0,dp(78),1);bp.setMargins(i==0?0:dp(4),0,i==3?0:dp(4),0);stats.addView(box,bp);}content.addView(stats);
    TextView recent=tv("Последние записи",18,MIST);recent.setTypeface(Typeface.DEFAULT,Typeface.BOLD);content.addView(recent,new LinearLayout.LayoutParams(-1,dp(42)));String[][] demo={{"characters","Аэлин","Странница северных земель"},{"locations","Элдар","Город на краю туманного моря"},{"factions","Орден Серебряного Пламени","Древний рыцарский орден"}};for(String[] d:demo) if(count(d[0])==0) card(d[1],d[2],"ПРИМЕР • можно изменить или удалить");
    TextView add=button("＋  Создать первую запись");LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(-1,dp(52));ap.setMargins(0,dp(8),0,0);content.addView(add,ap);add.setOnClickListener(v->chooseType());
  }
  int count(String key){try{return new JSONArray(db.getString(key,"[]")).length();}catch(Exception e){return 0;}}
  void showSection(int idx){String key=keys[idx], sec=sections[idx+1];header(sec,key);TextView add=button("＋  Добавить "+sec.toLowerCase());LinearLayout.LayoutParams ap=new LinearLayout.LayoutParams(-1,dp(50));ap.setMargins(0,0,0,dp(12));content.addView(add,ap);add.setOnClickListener(v->newEntry(key,sec));try{JSONArray a=new JSONArray(db.getString(key,"[]"));if(a.length()==0){TextView empty=tv("Пока здесь ничего нет. Создай первую запись.",14,MUTED);content.addView(empty,new LinearLayout.LayoutParams(-1,dp(70)));}for(int i=0;i<a.length();i++){JSONObject o=a.getJSONObject(i);card(o.optString("name","Без названия"),o.optString("desc",""),o.optString("meta",sec));}}catch(Exception e){}}
  void newEntry(String key,String sec){editDialog(null,"",key,sec);}
  void editDialog(String name,String desc){editDialog(name,desc,null,null);}
  void editDialog(String name,String desc,String key,String sec){
    if(key==null){key=findKeyByName(name);sec=findSectionByKey(key);} final String fkey=key==null?"characters":key, fsec=sec==null?"Персонажи":sec;LinearLayout form=new LinearLayout(this);form.setOrientation(LinearLayout.VERTICAL);form.setPadding(dp(4),0,dp(4),0);EditText n=new EditText(this);n.setHint("Название");n.setText(name==null?"":name);n.setTextColor(MIST);n.setHintTextColor(MUTED);n.setSingleLine();EditText d=new EditText(this);d.setHint("Описание");d.setText(desc==null?"":desc);d.setTextColor(MIST);d.setHintTextColor(MUTED);d.setMinLines(4);d.setGravity(Gravity.TOP);form.addView(n,new LinearLayout.LayoutParams(-1,dp(55)));form.addView(d,new LinearLayout.LayoutParams(-1,dp(130)));
    AlertDialog dialog=new AlertDialog.Builder(this).setTitle((name==null?"Новая запись":"Редактирование")+" • "+fsec).setView(form).setNegativeButton("Отмена",null).setPositiveButton("Сохранить",null).create();dialog.setOnShowListener(x->dialog.getButton(-1).setOnClickListener(v->{save(fkey,n.getText().toString().trim(),d.getText().toString().trim(),name);dialog.dismiss();if(fkey!=null)showSection(indexOf(fkey));}));dialog.show(); }
  String findKeyByName(String n){for(String k:keys)try{JSONArray a=new JSONArray(db.getString(k,"[]"));for(int i=0;i<a.length();i++)if(a.getJSONObject(i).optString("name").equals(n))return k;}catch(Exception e){}return "characters";}
  String findSectionByKey(String k){for(int i=0;i<keys.length;i++)if(keys[i].equals(k))return sections[i+1];return "Персонажи";}
  int indexOf(String k){for(int i=0;i<keys.length;i++)if(keys[i].equals(k))return i;return 0;}
  void save(String key,String name,String desc,String old){if(name.isEmpty())name="Без названия";try{JSONArray a=new JSONArray(db.getString(key,"[]"));if(old!=null&&!old.isEmpty()){for(int i=0;i<a.length();i++)if(a.getJSONObject(i).optString("name").equals(old)){JSONObject o=a.getJSONObject(i);o.put("name",name);o.put("desc",desc);db.edit().putString(key,a.toString()).apply();return;}}JSONObject o=new JSONObject();o.put("name",name);o.put("desc",desc);o.put("meta",findSectionByKey(key));a.put(o);db.edit().putString(key,a.toString()).apply();}catch(Exception e){}}
  void chooseType(){final String[] x=new String[keys.length];for(int i=0;i<x.length;i++)x[i]=sections[i+1];new AlertDialog.Builder(this).setTitle("Что создать?").setItems(x,(d,w)->newEntry(keys[w],x[w])).show();}
  void showSearch(){final EditText e=new EditText(this);e.setHint("Поиск по названию");e.setTextColor(MIST);e.setHintTextColor(MUTED);new AlertDialog.Builder(this).setTitle("Поиск").setView(e).setPositiveButton("Найти",(d,w)->search(e.getText().toString())).setNegativeButton("Отмена",null).show();}
  void search(String q){header("Поиск","Результаты");if(q.trim().isEmpty()){content.addView(tv("Введите запрос.",14,MUTED));return;}int hits=0;for(int i=0;i<keys.length;i++)try{JSONArray a=new JSONArray(db.getString(keys[i],"[]"));for(int j=0;j<a.length();j++){JSONObject o=a.getJSONObject(j);String n=o.optString("name"),ds=o.optString("desc");if((n+" "+ds).toLowerCase().contains(q.toLowerCase())){card(n,ds,sections[i+1]);hits++;}}}catch(Exception e){}if(hits==0)content.addView(tv("Ничего не найдено.",14,MUTED));}
}
