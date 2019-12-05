package net.learn2develop.myapplication111.SmsActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import net.learn2develop.myapplication111.AlarmHistoryActivity.AlarmHistoryActivity;
import net.learn2develop.myapplication111.AlarmProfile.AlarmProfileActivity;
import net.learn2develop.myapplication111.R;

public class AlarmSmsActivity extends AppCompatActivity {                // menu miedzy Activity
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.alarm_home:
                    Intent intent1 = new Intent(AlarmSmsActivity.this, AlarmProfileActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.history_alarm:
                    Intent intent2 = new Intent(AlarmSmsActivity.this, AlarmHistoryActivity.class);
                    startActivity(intent2);
                    return true;
                case R.id.sms_menu:
                    Intent intent3 = new Intent(AlarmSmsActivity.this, AlarmSmsActivity.class);
                    startActivity(intent3);

                    return true;
            }
            return false;
        }
    };

    private static final int  RESULT_PICK_CONTACT = 1;         // Contacts
    private TextView numbet_to_call;                            //
    private ImageButton button_contact;
    private ImageButton message_icon;
    // Contacts
    TextView name_sms_activity;
    TextView happy_birthday_info;
    TextView weeding_info;
    TextView like_info;
    TextView jubilee_partners_info;

    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 1;
    private static final String TAG = AlarmHistoryActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmsmsactivity);

        BottomNavigationView navView1 = findViewById(R.id.nav_view1);                       // иницилизация menu между mactivity
        navView1.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);    // -//-

        numbet_to_call = findViewById(R.id.number_to_call);
        name_sms_activity = findViewById(R.id.name_sms_activity);

        happy_birthday_info = findViewById(R.id.happy_birthday_info);
        weeding_info = findViewById(R.id.weeding_info);
        like_info = findViewById(R.id.like_info);
        jubilee_partners_info = findViewById(R.id.partner_info);

        button_contact = findViewById(R.id.button_contact);
        button_contact.setOnClickListener(new View.OnClickListener() {   // Кнопка Select
            @Override
            public void onClick(View v) {
                Intent intentContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intentContact, RESULT_PICK_CONTACT);
            }
        });

        message_icon = findViewById(R.id.message_icon);
        message_icon.setOnClickListener(new View.OnClickListener() {   // Кнопка Select
            @Override
            public void onClick(View view) {
                smsSendMessage(view);
            }
        });

        ImageButton weeding =  findViewById(R.id.weeding);
        weeding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmsMesengerLat();
            }
        });

        ImageButton happy_birthday =  findViewById(R.id.happy_birthday);
        happy_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmsMesengerHappyBirthday();
            }
        });

        ImageButton like = findViewById(R.id.like);
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmsMesengerLike();
            }
        });

        ImageButton partner = findViewById(R.id.partner);
        partner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSmsMesengerWeeding();
            }
        });

        setNameSms();
    }

    private void setNameSms() {
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        name_sms_activity.setText(name);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {           // Contacts
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RESULT_PICK_CONTACT:
                    contactPicked(data);
                    break;
            }
        } else {
            Toast.makeText(this, "Failed to pick contact", Toast.LENGTH_SHORT).show();
        }

    }

    private void contactPicked(Intent data){                                //Contacts
            Cursor cursor = null;

            try {
                String phoneNo = null;
                    Uri uri = data.getData();
                    cursor = getContentResolver().query(uri, null,null, null, null);
                    cursor.moveToFirst();
                    int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    phoneNo = cursor.getString(phoneIndex);

                    numbet_to_call.setText(phoneNo);



            }catch (Exception e){
                e.printStackTrace();
            }
        }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {          // Спрашиваес разришения на СМС
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (permissions[0].equalsIgnoreCase(Manifest.permission.SEND_SMS)
                        && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED) {
                    // Permission was granted.
                } else {
                    // Permission denied.
                    Log.d(TAG, getString(R.string.failure_permission));
                    Toast.makeText(AlarmSmsActivity.this,
                            getString(R.string.failure_permission),
                            Toast.LENGTH_SHORT).show();
                    // Disable the message button.
                    disableSmsButton();
                }
            }
        }
    }

    private void disableSmsButton() {
    }

    public void smsSendMessage(View view) {                     // Пересилаем текс в смс телефона
        checkForSmsPermission();

        EditText textView = (EditText) findViewById(R.id.number_to_call);
        // Use format with "smsto:" and phone number to create smsNumber.
        String smsNumber = String.format("smsto: %s",
                textView.getText().toString());


        // Find the sms_message view.
        EditText smsEditText = (EditText) findViewById(R.id.sms_message);
        // Get the text of the sms message.
        String sms = smsEditText.getText().toString();


        // Create the intent.
        Intent smsIntent = new Intent(Intent.ACTION_SENDTO);
        // Set the data for the intent as the phone number.
        smsIntent.setData(Uri.parse(smsNumber));
        // Add the message (sms) with the key ("sms_body").
        smsIntent.putExtra("sms_body", sms);


        // If package resolves (target app installed), send intent.
        if (smsIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(smsIntent);
        } else {
            Log.d(TAG , "Can't resolve app for ACTION_SENDTO Intent");
        }

    }

    private void checkForSmsPermission() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS) !=
                PackageManager.PERMISSION_GRANTED) {
            Log.d( TAG , getString(R.string.permission_not_granted));
            // Permission not yet granted. Use requestPermissions().
            // MY_PERMISSIONS_REQUEST_SEND_SMS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            // Permission already granted. Enable the message button.
            enableSmsButton();
        }
    }

    private void enableSmsButton() {
    }

    private void setSmsMesengerHappyBirthday() {
        String[] wordListOne = {"Młody, stary – bez znaczenia, " +
                        "Czas jest zawsze na marzenia, Spełniać je można w każdej chwili," +
                        " Bo to Ci życie najlepiej umili! Więc rób to, na co masz ochotę," +
                        " Obejdź świat na piechotę, Tańcz, maluj, głośno śpiewaj, Złych dni wcale już nie miewaj! ",
                            " Poszybować przez życie jak ptak\n" +
                            "niezależny, wolny, szczęśliwy...\n" +
                            "Poczuć prawdziwy życia smak...\n" +
                            "wszystkie kolory...\n" +
                            "jasne i ciemne odcienie...\n" +
                            "Mieć odwagę by wzbić się wysoko...\n" +
                            "przekroczyć sztywne granice...\n" +
                            "i nie bać sie spełniać swych marzeń...\n" +
                            "tego w dniu Twych urodzin Ci życzę!!! ",
                "Tyle wiedzy, doświadczenia,\n" +
                "Młody rzadko to docenia.\n" +
                "Tyle miejsc, tyle wrażeń,\n" +
                "I spełnionych Twoich marzeń.\n" +
                "Ale to nie koniec przecież,\n" +
                "Jesteś teraz w wieku kwiecie.\n" +
                "Bądź więc sobą wciąż, niezmiennie,\n" +
                "I uśmiechaj się codziennie!",
                "Życzę Ci, aby marzenia które skrywasz na dnie Twojego serca, " +
                "doczekały się spełnienia. Korzystając z okazji Twojego święta przesyłam Ci również garść nadziei i wiary,\n" +
                "aby dały Ci one siłę do pokonywania trudności jakie pojawiają się w codziennym życiu. ",
                "Jest w roku taki dzień,\n" +
                "W którym smutki idą w cień,\n" +
                "Więc z okazji tego dnia\n" +
                "Posłuchaj co Ci życzę ja:\n" +
                "Dużo zdrowia i radości,\n" +
                "Szczęścia w życiu i miłości,\n" +
                "Moc najpiękniejszych wrażeń\n" +
                "I spełnienia wszystkich marzeń. ",
                "Dziś są twoje urodziny. Choć się razem nie widzimy, " +
                        "to przesyłam Ci życzenia: Niech się spełnia Twe marzenia"};

        int oneLenght = wordListOne.length;

        int rand1 = (int) (Math.random() * oneLenght);

        EditText smsEditText = (EditText) findViewById(R.id.sms_message);

        String phrase = wordListOne[rand1];
        smsEditText.setText(phrase);
    }

    private void setSmsMesengerLike() {
        String[] wordListOne = {"Jestem gotów powiedzieć ci wszystko o sobie, " +
                "ponieważ wiem, że jesteś godny zaufania!",
                " I mówią, że nie ma prawdziwych przyjaciół, ale to nieprawda, bo jesteście!",
                " Jestem pewien, że jeśli spotka mnie nieszczęście, to ty będziesz pierwszym, który przyjdzie na ratunek!",
                " Cieszę się, że mam takiego przyjaciela jak ty!",
                " Nigdy się nie nudzisz!",
                " Możesz w każdej chwili wspierać, a ja to doceniam!",
                " Kiedy się kłócimy, martwię się bardzo długo!",
                " Od razu widać, że jesteś z inteligentnej rodziny",
                " Ciężko pracowałeś nad tym problemem i doceniam twoją opinię.",
                " Nawet najpiękniejsze słowa nie mogą wyrazić mojego podziwu dla ciebie",};

        int oneLenght = wordListOne.length;

        int rand1 = (int) (Math.random() * oneLenght);

        EditText smsEditText = (EditText) findViewById(R.id.sms_message);

        String phrase = wordListOne[rand1];
        smsEditText.setText(phrase);
    }


    private void setSmsMesengerWeeding() {


        String[] wordListOne = {" Abyście przeszli przez życie razem,\n" +
                "z uśmiechem na ustach,\n" +
                "szacunkiem i zrozumieniem\n" +
                "życzy…",
                " Zwykle uważa się, że wstąpienie w związek małżeński jest zwieńczeniem pewnego etapu\n" +
                        "w życiu dwojga ludzi. A tak naprawdę jest dokładnie na odwrót: małżeństwo jest dopiero\n" +
                        "początkiem wszystkiego, więc na tę nową drogę - wszystkiego najlepszego!",
                " Szczęśliwie sobie zaślubionym bukiet najgorętszych, z serca płynących życzeń:\n" +
                        "spełnienia najsłodszych marzeń, żaru nieustającej miłości, słońca w dniu powszednie\n" +
                        "i gwiazd w dni odświętne życzy…",
                "Na drodze życia są światła i mroki,\n" +
                        "raz szczęście jasne, raz smutek głęboki.\n" +
                        "Ale gdy razem idzie się we dwoje,\n" +
                        "łatwiej się znosi nawet ciężkie znoje.",
                " Bądźcie dla siebie i ziarnem pieprzu i szczyptą soli.\n" +
                        "I tym co cieszy, i tym co boli.\n" +
                        "Trwajcie razem, gdy miłość w rozkwicie\n" +
                        "i gdy ku jesieni swe kroki skieruje Wasze życie.",
                " W dniu Waszego ślubu - samych radosnych zdarzeń.\n" +
                        "Niech będzie on początkiem spełnienia Waszych marzeń.",
                "Przyjmijcie gorące życzenia, aby Wasza miłość była coraz piękniejsza, głębsza i by sprawiła,\n" +
                        "że Wasze życie nabierze szczególnego znaczenia i blasku.",
                " Drodzy Państwo Młodzi!Niechaj los Wam sprzyja, niosąc powodzenie!\n" +
                        "Idźcie przez życie z dobrocią w sercu, otoczeni ludźmi, których kochacie.\n" +
                        "Obdarzajcie się nawzajem nadzieją, miłością i radością.\n" +
                        "Z najlepszymi życzeniami na Nowej Drodze życia!",
                " Niech los zawsze ma Was w swojej opiece, a radość, która towarzyszy Wam tego dnia,\n" +
                        "niech pozostanie w Waszych sercach przez długie lata.",
                " Drodzy Państwo Młodzi! W tym niezwykłym dniu przyjmijcie od nas najszczersze życzenia.\n" +
                        "Kroczcie przez życie z podniesioną głową, a w codziennej gonitwie nigdy nie zapominajcie\n" +
                        "o tym, co najważniejsze.",
                " Niechaj szlachetność celów związek Wasz opromieni!\n" +
                        "Niechaj po latach wielu nic szczęścia Waszego nie zmieni!\n" +
                        "Zdrowie, miłość, dostatek niech w domu Waszym panują!\n" +
                        "Wesołe dziecięce uśmiechy niech nad tym wszystkim górują!",
                " W błękicie nieba i słońca blasku niech miłość Wasza rozkwita,\n" +
                        "Gdy wierność i prawość Was będzie prowadzić, do domu szczęście zawita.\n" +
                        "Radosny szczebiot dziecięcych głosików każdy dzień opromieni,\n" +
                        "a wspólne wzajemne zrozumienie nigdy się nie zmieni.",
                " Drodzy Nowożeńcy, życzymy Wam,\n" +
                        "aby Wasza wspólna droga przez życie, była usłana różami.\n" +
                        "Kolcami się nie przejmujcie, bo prawdziwa miłość\n" +
                        "przezwycięża wszystkie przeszkody."};

        int oneLenght = wordListOne.length;

        int rand1 = (int) (Math.random() * oneLenght);

        EditText smsEditText = (EditText) findViewById(R.id.sms_message);

        String phrase = wordListOne[rand1];
        smsEditText.setText(phrase);
    }


    public void setSmsMesengerLat () {

        // wyswietla smsTextRandom
        String[] wordListOne = {"Korzystając ze sposobności życzymy wszystkim zdrowia, szczęścia" +
                " i radości. Niech Państwa życie, nie tylko zawodowe, ale również to prywatne " +
                "obfituje w sukcesy i zawsze przynosi satysfakcję. ",
                "Znamy się już kupę czasu,\n" +
                        "więc dziś w bawełnę nie owijam,\n" +
                        "życzę Ci samych sukcesów,\n" +
                        "szczęścia i zdrowia również nie omijam.",
                " Współpracujemy razem nie rok nie dwa,\n" +
                        "czasem jest zabawnie że haha.\n" +
                        "A czasem są sytuacje trudne,\n" +
                        "ale nigdy nie są dla nas zgubne.\n" +
                        "Życzę Ci zdrowia, rozwoju osobistego,\n" +
                        "udanego życia rodzinnego, rosnącego majątku\n" +
                        "i wszystkiego najlepszego.",
                " Niech w życiu i biznesie,\n" +
                        "ciepła bryza ku sukcesowi Cię niesie.\n" +
                        "Niech zdrowie i energia z Ciebie tryskają,\n" +
                        "a nowe przychody konto bankowe zasilają.",
                " Mój drogi biznesowy partnerze,\n" +
                        "Powiem Ci bardzo szczerze,\n" +
                        "że w nasz wspólny sukces wierzę.\n" +
                        "Bądź silny i pewny siebie,\n" +
                        "nigdy nie ląduj na glebie.",
                " Niech biznes dobrze idzie,\n" +
                        "nic złego z niego nie wyjdzie.\n" +
                        "W zdrowiu zawsze żyj i radości,\n" +
                        "dodatkowo życzę wszelkiej pomyślności."};


        int oneLenght = wordListOne.length;


        int rand1 = (int) (Math.random() * oneLenght);


        EditText smsEditText = (EditText) findViewById(R.id.sms_message);

        String phrase = wordListOne[rand1];
        smsEditText.setText(phrase);
    }

}

