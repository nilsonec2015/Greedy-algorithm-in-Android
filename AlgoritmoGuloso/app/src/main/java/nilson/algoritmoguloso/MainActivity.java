package nilson.algoritmoguloso;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    LinearLayout fundo, job;
    TextView tvHead, tvOper, tvTituloJob;
    EditText etNumMaquinas;
    ArrayAdapter<String> maquinas;
    LinearLayout.LayoutParams defaultParams;

    int i=0,j=0,numMaquinas=0, JOM=0;

    ArrayList<ArrayList<ArrayList<Integer>>> triTempo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        triTempo = new ArrayList<>();

        defaultParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        defaultParams.setMargins(0, 0, 0, 0);

        fundo = new LinearLayout(this);
        fundo.setOrientation(LinearLayout.VERTICAL);

        job = new LinearLayout(this);
        job.setOrientation(LinearLayout.HORIZONTAL);


        tvHead = new TextView(this);
        tvHead.setText("Número de máquinas");
        tvHead.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        LinearLayout.LayoutParams headParams = (LinearLayout.LayoutParams)tvHead.getLayoutParams();
        headParams.setMargins(10,10,10,10);

        etNumMaquinas = new EditText(this);
        etNumMaquinas.setInputType(InputType.TYPE_CLASS_NUMBER);
        etNumMaquinas.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        etNumMaquinas.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3){
                if(cs.toString().length()>0)
                    addMaquinas();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(MainActivity.this,"Você ainda não pode criar operações", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //---bloco com o texto de numero de maquinas
        job = new LinearLayout(this);
        job.setOrientation(LinearLayout.HORIZONTAL);
        job.addView(tvHead);
        job.addView(etNumMaquinas);
        fundo.addView(job);
        //------------------------------------------

        //parte que mostra os jobs e as operações
        addJob();
        //---------------------------------------

        ScrollView sv = new ScrollView(this);
        sv.addView(fundo);
        setContentView(sv);


    }

    private void addJob(){
        //criando a telinha
        LinearLayout.LayoutParams defaultParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        i++;
        j=0;
        tvTituloJob = new TextView(this);
        tvTituloJob.setText("JOB "+i+"");
        tvTituloJob.setTextColor(Color.parseColor("#FFFFFF"));
        tvTituloJob.setBackgroundColor(Color.parseColor("#505550"));
        tvTituloJob.setLayoutParams(defaultParams);
        fundo.addView(tvTituloJob);
        //---------------------

        //criando as variáveis de cálculo
        triTempo.add(new ArrayList<ArrayList<Integer>>());

        //---------------------
    }

    /*
    são criados aqui objetos dinamicamente. Os spinners de escolha de máquina e os EditText de escolha
    de tempo têm listeners. portanto, eu tribuí um id para cada um deles dinamicamente de acordo com a
    sequencia em que foram criados. Com isto eu posso selecionar o id e por meio de uma conta simples
    saber a qual elemento da array de operações ele está referenciado
     */
    private void addOperacao(){
        j++;JOM++;
        //Log.i("guloso",""+j);
        triTempo.get(i-1).add(new ArrayList<Integer>());
        for(int c=0;c<numMaquinas;c++) {//adiciona todas as maquinas nesta operacao deste job
            triTempo.get(i-1).get(j-1).add(0);
        }
        //------------------------------

        //criando a telinha

        job = new LinearLayout(this);
        job.setOrientation(LinearLayout.HORIZONTAL);

        tvOper = new TextView(this);
        tvOper.setText(Html.fromHtml("O<sub>"+j+"</sub><sup>"+i+"</sup>"));
        tvOper.setTextColor(Color.parseColor("#000000"));
        tvOper.setGravity(Gravity.CENTER_VERTICAL);
        tvOper.setHeight(40);
        //tvOper.setLayoutParams(defaultParams);

        //máquinas e seus tempos
        final EditText etTempo = new EditText(this);
        etTempo.setHint("Tempo");
        etTempo.setId((i * 1000) + j);
        etTempo.setInputType(InputType.TYPE_CLASS_NUMBER);
        etTempo.setAutoLinkMask(0);
        etTempo.requestFocus();
        etTempo.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        etTempo.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3){
                if(cs.toString().length()>0) {
                    int indi = (etTempo.getId()) / 1000 ;
                    int indj = (etTempo.getId()) % 1000 ;
                    //Log.i("guloso","(i="+indi+")(j="+indj+")");
                    indi--;indj--;
                    triTempo.get(indi).get(indj).set(etTempo.getAutoLinkMask(), Integer.parseInt(etTempo.getText().toString()));
                    //Log.i("guloso", " - M"+(etTempo.getImeOptions()+1)+" tempo "+triTempo.get(indi).get(indj).get(etTempo.getImeOptions()));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Toast.makeText(MainActivity.this,"Você ainda não pode criar operações", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        etTempo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {

                } else {

                }
            }
        });

        final Spinner etMaquina = new Spinner(this);
        etMaquina.setAdapter(maquinas);
        etMaquina.setLayoutParams(defaultParams);
        etMaquina.setId((i*100)+j);
        etMaquina.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int indi = (etMaquina.getId()) / 100 ;
                int indj = (etMaquina.getId()) % 100 ;
                //Log.i("guloso","(i="+indi+")(j="+indj+")");
                etTempo.setAutoLinkMask(position);
                indi--;indj--;
                for(int c=0;c<numMaquinas;c++) {
                    if(triTempo.get(indi).get(indj).get(c)>0) {
                        triTempo.get(indi).get(indj).set(position, triTempo.get(indi).get(indj).get(c));
                        triTempo.get(indi).get(indj).set(c,0);
                        break;
                    }
                }
                //Log.i("guloso"," - M" + (position + 1) + " tempo " + triTempo.get(indi).get(indj).get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        job.addView(tvOper);
        job.addView(etMaquina);
        job.addView(etTempo);
        fundo.addView(job);

    }

    private void addMaquinas(){
        //definir a array de acordo com o numero digitado
        numMaquinas= Integer.parseInt(etNumMaquinas.getText().toString());
        String[] Stringmaquinas = new String[numMaquinas];
        for(int i=0;i<numMaquinas;i++){
            Stringmaquinas[i]="Máquina "+(i+1);
        }
        maquinas = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Stringmaquinas);
        maquinas.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }


    private void printArray(ArrayList<ArrayList<ArrayList<Integer>>> array) {
        String msg="";
        for (int a=0;a<array.size();a++){
            msg+="Job("+(a+1)+"){\n";
            for (int b=0;b<array.get(a).size();b++){
                msg+="  OP("+(b+1)+"){";
                for (int c=0;c<array.get(a).get(b).size();c++){
                        msg+="M"+(c+1)+"("+array.get(a).get(b).get(c)+") ";
                }
                msg+="}\n";
            }
            msg+="}\n";
        }
        Log.i("guloso",msg);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.nova_operacao) {
            //Toast.makeText(MainActivity.this,"Você ainda não pode criar operações", Toast.LENGTH_SHORT).show();
            if(etNumMaquinas.getText().length()==0) {
                Toast.makeText(MainActivity.this,"Primeiro defina o número de máquinas", Toast.LENGTH_SHORT).show();
                return true;
            }else {
                etNumMaquinas.setEnabled(false);
                addOperacao();
                return true;
            }
        }
        if (id == R.id.novo_job) {
            if(etNumMaquinas.getText().length()==0) {
                Toast.makeText(MainActivity.this,"Primeiro defina o número de máquinas", Toast.LENGTH_SHORT).show();
                return true;
            }else {
                if(j==0){
                    Toast.makeText(MainActivity.this,"O ultimo JOB ainda está sem Operações", Toast.LENGTH_SHORT).show();
                }else{
                    etNumMaquinas.setEnabled(false);
                    addJob();
                }

                return true;
            }
        }
        if (id == R.id.iniciar) {
            if(j>0) {
                Intent seguir = new Intent(MainActivity.this, Resultado.class);
                seguir.putExtra("tritempo",triTempo);
                seguir.putExtra("JOM",JOM);
                startActivity(seguir);
            }else{
                Toast.makeText(MainActivity.this,"O ultimo JOB ainda está sem Operações", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
