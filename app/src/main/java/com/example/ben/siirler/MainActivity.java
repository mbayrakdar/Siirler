package com.example.ben.siirler;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private ListView lv;
    private ListView lv2;
    public List<String> list;
    public ArrayList liste = new ArrayList();
    public ArrayList sairliste = new ArrayList();
    public ArrayList lsiir = new ArrayList();
    public ArrayList mesajsiirsair = new ArrayList();
    public ArrayList favlsiir = new ArrayList();
    public ArrayAdapter<String> adapter;
    public ArrayAdapter<String> adapter2;
    public ArrayAdapter<String> adapter4;
    public ProgressDialog progressDialog;
    public ProgressDialog progressDialog2;
    public ProgressDialog progressDialog3;
    public ProgressDialog progressDialog4;
    public ProgressDialog progressDialog5;
    public ProgressDialog progressDialog6;
    public TextView t_sair;
    public TextView t_baslik;
    public TextView t_siir;
    public TextView t_siirleri;
    public static final String siirUrl = "http://siir.sitesi.web.tr/";
    public String sairadi;
    public String turkcesairadi;
    public String siiradi;
    public String turkcesiiradi;
    FloatingActionButton fab;
    public ListView dataListele;
    public String urlsiir;
    public String siir;
    public String sair;
    public String fdf;
    public int pos;
    AlertDialog.Builder builderSingle;
    public ImageView img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        FragmentPoems fragmentPoems = new FragmentPoems();
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container, fragmentPoems);
        ft.commit();

        setSupportActionBar(toolbar);


        new FetchPoem().execute();
        builderSingle = new AlertDialog.Builder(MainActivity.this);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    private class FetchPoem extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setTitle("ŞAİRLER");
            progressDialog.setMessage("Lütfen bekleyiniz..");
            progressDialog.setIndeterminate(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect(siirUrl + "sairler.html").get();
                Elements oyunadi = doc.select("div[class=siir]");
                for (int i = 0; i < oyunadi.size(); i++) {
                    liste.add(oyunadi.get(i).text());
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            lv = (ListView) findViewById(R.id.list_view);
            adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, liste);
            lv.setAdapter(adapter);
            progressDialog.dismiss();

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    turkcesairadi = liste.get(position).toString();
                    if (liste.get(position).toString().contains(" ")) {
                        sairadi = liste.get(position).toString().toLowerCase().replace(" ", "-").replace(".", "")
                                .replace("ı", "i").replace("ç", "c").replace("ğ", "g").replace("ö", "o").replace("ş", "s").replace("ü", "u");
                    }


                    new FetchSiir().execute();
                    FragmentSiirler fragmentsiirler = new FragmentSiirler();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.container, fragmentsiirler);
                    ft.commit();

                    liste.clear();

                }
            });

        }
    }

    private class FetchSiir extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog2 = new ProgressDialog(MainActivity.this);
            progressDialog2.setTitle(turkcesairadi + " Şiirleri");
            progressDialog2.setMessage("Lütfen bekleyiniz..");
            progressDialog2.setIndeterminate(false);
            progressDialog2.show();

        }


        @Override
        protected Void doInBackground(Void... params) {

            try {
                Document doc = Jsoup.connect(siirUrl + sairadi + "/").get();
                Elements oyunadi = doc.select("div[class=siir]");

                for (int i = 0; i < oyunadi.size(); i++) {

                    sairliste.add(oyunadi.get(i).text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            lv2 = (ListView) findViewById(R.id.list_view2);
            adapter2 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, sairliste);
            lv2.setAdapter(adapter2);
            t_siirleri = (TextView) findViewById(R.id.txtsiirleri);
            t_siirleri.setText(turkcesairadi + " Şiirleri");
            progressDialog2.dismiss();
            lv2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    turkcesiiradi = sairliste.get(position).toString();
                    UrlDuzenle(turkcesairadi, turkcesiiradi);
                    fdf = UrlDuzenle(turkcesairadi, turkcesiiradi);
                    new FetchSiirinKendisi().execute();
                    FragmentSiir fragmentsiir = new FragmentSiir();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.container, fragmentsiir);
                    ft.commit();


                    sairliste.clear();

                }
            });

        }
    }

    private class FetchSiirinKendisi extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog3 = new ProgressDialog(MainActivity.this);
            progressDialog3.setTitle(turkcesiiradi);
            progressDialog3.setMessage("Lütfen bekleyiniz..");
            progressDialog3.setIndeterminate(false);
            progressDialog3.show();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Document doc = Jsoup.connect(siirUrl + fdf + ".html").get();
                Elements oyunadi = doc.select("div[class=text]");
                lsiir.add(oyunadi.text());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            t_sair = (TextView) findViewById(R.id.textyazar);
            t_baslik = (TextView) findViewById(R.id.textbaslik);
            t_siir = (TextView) findViewById(R.id.textsiir);
            img = (ImageView) findViewById(R.id.sairImage);

            t_siir.setMovementMethod(new ScrollingMovementMethod());

            String dogruad = fdf.split("/")[0].replace("-", "_");
            Resources res = getResources();
            String mDrawableName = dogruad;
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID);
            img.setImageDrawable(drawable);

            String bolunecekSiir = lsiir.get(0).toString();
            String bolunmusSiir = "";
            String[] r = bolunecekSiir.split("(?=\\p{Lu})");
            for (int i = 0; i < r.length; i++) {
                bolunmusSiir += r[i] + "\n";
            }

            t_sair.setText(turkcesairadi);
            t_baslik.setText(turkcesiiradi);
            t_siir.setText(bolunmusSiir);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Database database = new Database(MainActivity.this);
                    list = database.FavoriListele();
                    String listee = "";
                    for (int i = 0; i < list.size(); i++) {
                        listee += list.get(i).toString().split("-")[1] + "/";
                    }
                    if (list.size() > 0) {
                        int a = Integer.parseInt(list.get(pos).toString().split("-")[0].trim());
                        if (listee.contains(turkcesiiradi)) {
                            OpenDialogFavoriSil(MainActivity.this);
                        } else {
                            String eklenenAd = turkcesiiradi;
                            String eklenenSair = turkcesairadi;
                            database.FavoriEkle(eklenenAd, eklenenSair);

                            Snackbar.make(view, "Favorilere eklendi", Snackbar.LENGTH_LONG)
                                    .setAction("Action", null).show();
                            fab.setImageResource(R.drawable.ibe);
                        }
                    } else {
                        String eklenenAd = turkcesiiradi;
                        String eklenenSair = turkcesairadi;
                        database.FavoriEkle(eklenenAd, eklenenSair);

                        Snackbar.make(view, "Favorilere eklendi", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        fab.setImageResource(R.drawable.ibe);
                    }

                }
            });
            progressDialog3.dismiss();

            lsiir.clear();


        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            OpenSettingsDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_poem) {

            new FetchPoem().execute();
            FragmentPoems fragmentPoems = new FragmentPoems();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, fragmentPoems);
            ft.commit();

        } else if (id == R.id.nav_fav) {

            new ListeleFav().execute();
            FragmentFavoriler fragmentfav = new FragmentFavoriler();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, fragmentfav);
            ft.commit();

        } else if (id == R.id.nav_message) {
            new MesajdanSiir().execute();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent(MainActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("EXIT", true);
            startActivity(intent);
        }
        return super.onKeyDown(keyCode, event);
    }

    public String UrlDuzenle(String sair, String siir) {
        siiradi = siir.toLowerCase();

        if (siir.contains(" ")) {
            siiradi = siiradi.replace(" ", "-");
        }
        if (siir.contains(".")) {
            siiradi = siiradi.replace(".", "");
        }
        if (siir.contains(",")) {
            siiradi = siiradi.replace(",", "");
        }
        if (siir.contains("!")) {
            siiradi = siiradi.replace("!", "");
        }
        if (siir.contains("?")) {
            siiradi = siiradi.replace("?", "");
        }
        if (siir.contains("-")) {
            siiradi = siiradi.replace("-", "");
        }
        if (siir.contains("(" + " ")) {
            siiradi = siiradi.replace("(" + " ", "");
        }
        if (siir.contains("(")) {
            siiradi = siiradi.replace("(", "");
        }
        if (siir.contains(")")) {
            siiradi = siiradi.replace(")", "");
        }

        if (siir.contains("/" + " ")) {
            siiradi = siiradi.replace("/" + " ", "");
        }
        if (siir.contains("." + " ")) {
            siiradi = siiradi.replace("." + " ", "");
        }

        siiradi = siiradi.replace("ı", "i").replace("ç", "c").replace("ğ", "g").replace("ö", "o").replace("ş", "s").replace("ü", "u");
        /*if (siiradi.endsWith("-")){
            siiradi = siiradi.substring(0, siiradi.length() - 1);
        }*/

        sairadi = sair.toLowerCase();
        if (sairadi.contains(".")) {
            sairadi = sairadi.replace(".", "");
        }
        if (sairadi.contains(" ")) {
            sairadi = sairadi.replace(" ", "-").replace("ı", "i").replace("ç", "c").replace("ğ", "g").replace("ö", "o").replace("ş", "s").replace("ü", "u");
        }

        return sairadi + "/" + siiradi;
    }

    private class ListeleFav extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog4 = new ProgressDialog(MainActivity.this);
            progressDialog4.setTitle("Favoriler");
            progressDialog4.setMessage("Listeleniyor..");
            progressDialog4.setIndeterminate(false);
            progressDialog4.show();

        }


        @Override
        protected Void doInBackground(Void... params) {
            Database database = new Database(MainActivity.this);
            list = database.FavoriListele();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            dataListele = (ListView) findViewById(R.id.fav_lview);
            adapter4 = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, list);
            dataListele.setAdapter(adapter4);
            progressDialog4.dismiss();

            dataListele.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String item = list.get(position).toString();
                    String siirsair = item.split("-")[1];
                    String[] siirsair1 = siirsair.split("\\(");
                    sair = siirsair1[1].replace(")", "").trim();
                    siir = siirsair1[0].trim();
                    urlsiir = UrlDuzenle(sair, siir);

                    pos = position;


                    new FavoriSiir().execute();
                    FragmentSiir fragmentsiir = new FragmentSiir();
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction ft = fragmentManager.beginTransaction();
                    ft.replace(R.id.container, fragmentsiir);
                    ft.commit();

                }
            });
        }
    }

    private class FavoriSiir extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


            progressDialog5 = new ProgressDialog(MainActivity.this);
            progressDialog5.setTitle("Favori Şiir");
            progressDialog5.setMessage("Lütfen bekleyiniz..");
            progressDialog5.setIndeterminate(false);
            progressDialog5.show();

        }


        @Override
        protected Void doInBackground(Void... params) {

            try {


                Document doc = Jsoup.connect(siirUrl + urlsiir + ".html").get();
                Elements elements = doc.select("div[class=text]");
                favlsiir.add(elements.text());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            t_sair = (TextView) findViewById(R.id.textyazar);
            t_baslik = (TextView) findViewById(R.id.textbaslik);
            t_siir = (TextView) findViewById(R.id.textsiir);
            img = (ImageView) findViewById(R.id.sairImage);

            t_siir.setMovementMethod(new ScrollingMovementMethod());

            String dogruad = urlsiir.split("/")[0].replace("-", "_");
            Resources res = getResources();
            String mDrawableName = dogruad;
            int resID = res.getIdentifier(mDrawableName, "drawable", getPackageName());
            Drawable drawable = res.getDrawable(resID);
            img.setImageDrawable(drawable);

            String bolunecekSiir = favlsiir.get(0).toString();
            String bolunmusSiir = "";
            String[] r = bolunecekSiir.split("(?=\\p{Lu})");
            for (int i = 0; i < r.length; i++) {
                bolunmusSiir += r[i] + "\n";
            }

            t_sair.setText(sair);
            t_baslik.setText(siir);
            t_siir.setText(bolunmusSiir);

            fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setImageResource(R.drawable.ibe);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Database database = new Database(MainActivity.this);
                    list = database.FavoriListele();
                    String listee = "";
                    for (int i = 0; i < list.size(); i++) {
                        listee += list.get(i).toString().split("-")[1] + "/";
                    }
                    int a = Integer.parseInt(list.get(pos).toString().split("-")[0].trim());
                    if ((listee.contains(siir))) {
                        database.FavoriSil(a);
                        fab.setImageResource(R.drawable.ib);
                        Snackbar.make(view, "Favorilerden kaldırıldı", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();

                    } else {
                        String eklenenAd = turkcesiiradi;
                        String eklenenSair = turkcesairadi;
                        database.FavoriEkle(eklenenAd, eklenenSair);

                        Snackbar.make(view, "Favorilere eklendi", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                        fab.setImageResource(R.drawable.ibe);
                    }

                }
            });
            progressDialog5.dismiss();
            favlsiir.clear();
        }
    }

    private class MesajdanSiir extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog6 = new ProgressDialog(MainActivity.this);
            progressDialog6.setTitle("Bir şiiriniz var :)");
            progressDialog6.setMessage("Lütfen bekleyiniz..");
            progressDialog6.setIndeterminate(false);
            progressDialog6.show();

        }


        @Override
        protected Void doInBackground(Void... params) {


            try {

                Document doc = Jsoup.connect(siirUrl + "siirler-800" + ".html").get();
                Elements oyunadi = doc.select("div[class=siir]");
                for (int i = 0; i < oyunadi.size(); i++) {

                    mesajsiirsair.add(oyunadi.get(i).text());
                }


            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            Random rnd = new Random();
            int sayi = rnd.nextInt(mesajsiirsair.size());
            String item = mesajsiirsair.get(sayi).toString();
            String[] mesajsiirlerlistesi = item.split("\\(");
            String mesajsiir = mesajsiirlerlistesi[0].trim();
            String mesajsair = mesajsiirlerlistesi[1].split("\\)")[0];
            String MesajSiirURL = UrlDuzenle(mesajsair, mesajsiir);

            turkcesairadi = mesajsair;
            turkcesiiradi = mesajsiir;
            fdf = MesajSiirURL;
            new FetchSiirinKendisi().execute();
            FragmentSiir sii = new FragmentSiir();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.container, sii);
            ft.commit();

            progressDialog6.dismiss();
            mesajsiirsair.clear();


        }
    }

    private void OpenDialogFavoriSil(MainActivity mainActivity) {
        new AlertDialog.Builder(mainActivity).setTitle("Favori Silme Onayı")
                .setMessage("Emin misiniz?")
                .setPositiveButton("Favorilere git", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ListeleFav().execute();
                        FragmentFavoriler fragmentfav = new FragmentFavoriler();
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.container, fragmentfav);
                        ft.commit();
                    }
                })
                .setNeutralButton("Vazgeç", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create().show();
    }

    public void OpenSettingsDialog() {

        final ArrayAdapter<String> Settings = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        Settings.addAll("Arka Plan");
        builderSingle.setAdapter(Settings, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = Settings.getItem(which);
                if (strName == "Arka Plan") {
                    OpenArkaPlanDialog();
                }
            }
        });
        builderSingle.show();
    }

    public void OpenArkaPlanDialog() {

        final ArrayAdapter<String> ArkaPlan = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1);
        ArkaPlan.addAll("Toz Pembe", "Turuncu", "Gri", "Yeşil", "Mor", "Mavi");
        builderSingle.setAdapter(ArkaPlan, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String strName = ArkaPlan.getItem(which);
                if (strName == "Toz Pembe") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(R.color.tozpembe));
                } else if (strName == "Turuncu") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(R.color.turuncu));
                } else if (strName == "Gri") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                } else if (strName == "Yeşil") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(R.color.yesil));
                } else if (strName == "Mor") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(R.color.mor));
                } else if (strName == "Mavi") {
                    View someView = findViewById(R.id.container);
                    View root = someView.getRootView();
                    root.setBackgroundColor(getResources().getColor(R.color.mavi));
                }
            }
        });
        builderSingle.show();
    }

}