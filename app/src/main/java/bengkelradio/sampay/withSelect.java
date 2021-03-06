package bengkelradio.sampay;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class withSelect extends AppCompatActivity {
    ImageButton confirm;
    ImageButton cancel;
    EditText withAmt;
    TextView bank;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_select);

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child("user").child(currentUser.getUid());
        bank = (TextView)findViewById(R.id.bank);
        withAmt = (EditText)findViewById(R.id.withAmt);
        confirm = (ImageButton) findViewById(R.id.confirm);

        //post database value to TextView
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String rekening = dataSnapshot.child("bank").getValue().toString();
                bank.setText(rekening);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateMoney addMoney = new updateMoney("withdraw");
                int withdrawAmmount = Integer.parseInt(withAmt.getText().toString());
                addMoney.moneyValue(withdrawAmmount);
                Intent intent = new Intent(withSelect.this, withList.class);
                intent.putExtra(Intent.EXTRA_TEXT, Integer.toString(withdrawAmmount));
                startActivity(intent);
            }
        });

        cancel = (ImageButton) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(withSelect.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}
