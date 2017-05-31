package com.worshipsearcher.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;

import com.worshipsearcher.R;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        /*TextView infoTex = (TextView) findViewById(R.id.infoTextView);
        infoTex.setText("Az Istentisztelet Kereső segítségével a Magyarországi Evangélikus Egyház " +
                "templomai és Istentiszteletei böngészhetők.\n\n" +
                "Lehetőség van az összes templom megtekintésére a térképen, " +
                "itt a markerekre kattintva ki lehet választani, hogy " +
                "a legközelebbi alkalmora, a templom képére, vagy egy hónap összes alkalmára vagyunk-e kíváncsiak.\n\n");
        infoTex.append("Az Istentiszteleti helyeket meg lehet tekinteni egy listában is." +
                "Ha a GPS vevő be van kapcsolva, akkor a templomakat távolság szerint rendezve, " +
                "egyébként alfabetikusan rendezve tekinthetjük meg " +
                "itt lehetőség van a szöveges keresésre is a felső kereső mező segítségével " +
                "a lista elemekre történő rövid koppintás után megjelennek a templom alkalmai havi nézetben"+
                "hosszú koppintás után választhatunk a legközelebbi alkalom, a templom képének megnézésére " +
                "vagy a megtekinthetjük a templomot a térképen a Google Maps segítségével, itt navigácciót is kérhetünk a templomhoz");
        infoTex.append("Lehetőség van továbbá dátum szerint keresni, a naptárban ki kell választani egy vasárnapot, " +
                "és a program listázza annak a vasárnapnak az alkalmait az összes templomban");
        */
        String head = "<html><head></head>";
        String body = "<body bgcolor=\"#ecdf82\"><font size=\"5%\" color=\"#0d164a\"><p align=\"justify\">"+ "Az Istentisztelet-Kereső jelenlegi verziójának segítségével a Magyarországi Evangélikus Egyház " +
                "templomai és Istentiszteletei böngészhetők." + "<br><br>" +
                "Lehetőség van az összes templom megtekintésére a térképen, " +
                "itt a jelölőkre kattintva ki lehet választani, hogy " +
                "a legközelebbi alkalomra, a templom képére, vagy egy hónap összes alkalmára vagyunk-e kíváncsiak. " + "<br><br>" +
                "Az Istentiszteleti helyeket meg lehet tekinteni egy listában is. " +
                "Ha a GPS vevő be van kapcsolva, akkor a templomakat távolság szerint rendezve, " +
                "egyébként az ábécé szerint rendezve tekinthetjük meg. " +
                "Lehetőségünk van a szöveges keresésre is a felső kereső mező segítségével, " +
                "a lista elemekre történő rövid koppintás után megjelennek a templom alkalmai havi nézetben. " +
                "Hosszú koppintás után három lehetőség közül válaszhatunk. Ezek a legközelebbi alkalom lekérése, a templom képének megtekintése, illetve a templom megjelenítése a térképen " +
                "a Google Maps segítségével, ahol navigációt is kérhetünk a templomhoz. " + "<br><br>" +
                "Lehetőség van továbbá dátum szerint keresni, a naptárban ki kell választani egy vasárnapot, " +
                "és a program listázza annak a vasárnapnak az alkalmait az összes templomban."
                + "<br><br>"
                + "Ha észrevétle van, adathibákat, adathiányokat tapasztal vegye fel a kapcsolatot a fejlesztővel, Szalai Mihállyal a " +"</font><font color=\"#0000EE\" size=\"5%\"><a mailto=\"szalaim@gain.nyme.hu\">szalaim@gain.nyme.hu</a></font>"  +"<font size=\"5%\" color=\"#0d164a\">" + " email címre."+ "</font></body>";
        String foot = "</html>";
        String summary = head + body + foot;
        WebView webView = (WebView) findViewById(R.id.webView);
        webView.loadData(summary, "text/html; charset=utf-8", "utf-8");

    }
}
