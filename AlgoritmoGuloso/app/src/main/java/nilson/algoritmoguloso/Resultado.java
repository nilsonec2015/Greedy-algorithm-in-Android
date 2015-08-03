package nilson.algoritmoguloso;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;



public class Resultado extends ActionBarActivity {


    ArrayList<ArrayList<ArrayList<Integer>>> triTempo;
    ArrayList<ArrayList<ArrayList<Integer>>> triInicio;

    int dimX=3, dimY=3, dimZ=3;
    int tempoOtimo=0;
    int JOM;

    FrameLayout fundo;
    int ht,wt;
    ArrayList<Integer> graphicBarra = new ArrayList<>();
    ArrayList<Integer> graphicX = new ArrayList<>();
    ArrayList<Integer> graphicY = new ArrayList<>();
    ArrayList<Integer> graphicInicio = new ArrayList<>();
    ArrayList<Integer> graphicFim = new ArrayList<>();

    TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        tvResult = (TextView) findViewById(R.id.tvResult);

        telaDeDesenho();
        //triTempo=criaArrayTempo();
        Bundle bundle = getIntent().getExtras();
        triTempo = (ArrayList<ArrayList<ArrayList<Integer>>>) bundle.get("tritempo");
        JOM = bundle.getInt("JOM",0);//número total de Operações a serem realizadas

        dimX=triTempo.size();
        dimY=triTempo.get(0).size();
        dimZ=triTempo.get(0).get(0).size();
        for(int i=0;i<dimX;i++)
            if (dimY < triTempo.get(i).size())
                dimY = triTempo.get(i).size();

        criaArrayInicio();
        try {
            calcular();
        }catch (Exception e){Log.i("guloso", "Erro ao calcular");}
        try {
            desenhar();
        }catch(Exception e){Log.i("guloso", "Erro ao desenhar");}

    }

    /*
    define a area de desenho
    captura a largura da tela e acrescenta uma margem para os nomes das maquinas e tambem
     uma pequena margem à direita
    */
    private void telaDeDesenho() {
        fundo = (FrameLayout) findViewById(R.id.fundo);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        ht = displaymetrics.heightPixels;
        wt = displaymetrics.widthPixels;
        wt=wt-dp(110, this);

    }

    /*
    converte px para dp
     */
    public int dp(int px, Context context){
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        int dp = (int) (px / (metrics.densityDpi / 160f));
        return dp;
    }
    /*
    converte px para % de largura de tela relativo ao valor máximo de tempo do gráfico
     */
    public int o(int px){
        int dp = px*wt/tempoOtimo;
        return dp;
    }

    /*
    cria cores aleatórias
     */
    public String random_color() {
        String letters = "0123456789ABCDEF";
        String color = "#";
        int index;
        for(int i = 0; i < 6; i++) {
            index = (int) (Math.random()*15);
            color += letters.charAt(index);
        }
        return color;
    }

    /*
    Cria textViews para representar as máquinas, operações e tempos

     */
    private void desenhar(){
        FrameLayout.LayoutParams defaultParams;
        //TextView das maquinas
        for(int i=0;i<dimZ;i++){
            defaultParams = new FrameLayout.LayoutParams(dp(80, this), dp(40, this));
            defaultParams.gravity = Gravity.BOTTOM;
            defaultParams.setMargins(dp(10, this), 0, 0, dp(70,this)*(i+1));
            TextView iMaquina = new TextView(this);
            iMaquina.setText("M" + (i + 1));
            iMaquina.setLayoutParams(defaultParams);

            fundo.addView(iMaquina);
        }
        for(int i=0;i<(dimX*dimY);i++){
            //TextView dos tempos
            int tam=(o(graphicFim.get(i)-graphicInicio.get(i))>15)?o(graphicFim.get(i)-graphicInicio.get(i)):15;
            defaultParams = new FrameLayout.LayoutParams(tam, dp(30, this));
            //Log.i("guloso","tamanho do "+(graphicFim.get(i)-graphicInicio.get(i))+"->"+o(graphicFim.get(i)-graphicInicio.get(i)));
            defaultParams.gravity=(Gravity.LEFT | Gravity.BOTTOM);
            defaultParams.setMargins(o(graphicInicio.get(i))+dp(90, this), 0, 0, 0);
            TextView iTempo = new TextView(this);
            iTempo.setGravity(Gravity.END);
            iTempo.setTextSize(8.0f);

            iTempo.setTextColor(Color.parseColor("#000000"));
            iTempo.setText(""+graphicFim.get(i));
            iTempo.setLayoutParams(defaultParams);
            fundo.addView(iTempo);

            //TextView dos blocos
            defaultParams = new FrameLayout.LayoutParams(o(graphicFim.get(i)-graphicInicio.get(i)), dp(40, this));
            defaultParams.gravity = Gravity.BOTTOM;

            defaultParams.setMargins(o(graphicInicio.get(i))+dp(90, this), 0, 0, dp(70,this)*(graphicBarra.get(i)));
            TextView iOperacao = new TextView(this);
            iOperacao.setBackgroundColor(Color.parseColor(random_color()));
            iOperacao.setTextColor(Color.parseColor("#000000"));
            iOperacao.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
            iOperacao.setTextSize(8.0f);
            iOperacao.setText("O" + graphicY.get(i)+graphicX.get(i));
            iOperacao.setLayoutParams(defaultParams);
            fundo.addView(iOperacao);
        }
    }
    /*
        seta as variáveis que vão conter o formato das barras do gráfico
     */
    private void graphicBarra(int maquina, int x, int y, int inicio, int fim){
        graphicBarra.add(maquina);
        graphicX.add(x);
        graphicY.add(y);
        graphicInicio.add(inicio);
        graphicFim.add(fim);
    }

    private void calcular() {
        String msg="";
        int menorTempo;//tempo acumulado da operacao+tempo da operacao
        int menorTempo2;//tempo da operacao
        int menorTempoIndiceY0=0;
        int menorTempoMaquina=0;//numero da maquina

        //calculando a iteracao 1
        for(int loop=0;loop<(JOM);loop++) {
            msg="ITERACAO " + (loop + 1);
            Log.i("guloso", msg);
            tvResult.append(msg + "\n");
            menorTempo = 99999;
            menorTempo2 = 99999;
            msg=" ";
            for (int i = 0; i < dimX; i++) {//tamanho da vertical
                for (int j = 0; j < dimZ; j++) {//numero de maquinas
                    try {
                        if (triTempo.get(i).get(0).get(j) > 0) {
                            msg +="O"+(dimX - triTempo.get(i).size() + 1)+(i+1)+"(M"+(j+1)+" ;"+(triInicio.get(i).get(0).get(j) + triTempo.get(i).get(0).get(j)) + ") ";
                            if (menorTempo > (triInicio.get(i).get(0).get(j) + triTempo.get(i).get(0).get(j))) {
                                menorTempo = (triInicio.get(i).get(0).get(j) + triTempo.get(i).get(0).get(j));
                                menorTempo2 = triTempo.get(i).get(0).get(j);
                                menorTempoMaquina = (j);
                                menorTempoIndiceY0 = i;
                            }
                        }
                    }catch(Exception e){}
                }
            }
            Log.i("guloso", msg);
            tvResult.append(msg+"\n");
            if(menorTempo==99999){
                tvResult.append(msg+"Iterações encerradas\n");
                return;
            }
            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_LONG).show();
            tempoOtimo=menorTempo;
            msg = "     Menor tempo: O" + (dimX - triTempo.get(menorTempoIndiceY0).size() + 1) + (menorTempoIndiceY0 + 1) + "(M" + (menorTempoMaquina + 1) + ";" + menorTempo2 + ") valendo " + menorTempo + " no momento";
            //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
            Log.i("guloso", msg);
            tvResult.append(msg+"\n______________________\n");

            //desenhando no grafico
            graphicBarra(menorTempoMaquina + 1,(menorTempoIndiceY0 + 1),(dimX - triTempo.get(menorTempoIndiceY0).size() + 1), menorTempo - menorTempo2, menorTempo);

            Log.i("guloso", "vamos atualizar a maquina " + (menorTempoMaquina + 1));
            //atualizando inicio do job em questao
            Log.i("guloso", "estamos no indice " + (menorTempoIndiceY0+1));
            for (int i = 1; i < dimY; i++) {//percorre horizontalmente examinando as colunas do menor tempo
                for (int j = 0; j < dimZ; j++) {//numero de maquinas
                    try {
                        if (triTempo.get(menorTempoIndiceY0).get(i).get(j) > 0) {
                            Log.i("guloso", "estamos na maquina " + (j + 1) + " valor " + triInicio.get(menorTempoIndiceY0).get(i).get(j) + " -> " + menorTempo);
                            triInicio.get(menorTempoIndiceY0).get(i).set(j, menorTempo);
                        }
                    } catch (Exception e) {
                    }
                }
            }
            //atualizando o inicio das maquinas nos outros jobs

            for (int i = 0; i < dimX; i++) {//percorre verticalmente examinando as linhas horizontais
                if (i != menorTempoIndiceY0) {//não passa pela linha do menor tempo
                    Log.i("guloso", "estamos no indice " + (i+1));
                    for (int j = 0; j < dimY; j++) {
                        for (int k = 0; k < dimZ; k++) {
                            try {
                                if (k == menorTempoMaquina && triTempo.get(i).get(j).get(k) > 0) {
                                    if (menorTempo > triInicio.get(i).get(j).get(k)) {
                                        Log.i("guloso", "estamos na maquina " + (k + 1) + " valor " + triInicio.get(i).get(j).get(k) + " -> " + menorTempo);
                                        triInicio.get(i).get(j).set(k, menorTempo);
                                    }
                                }
                            } catch (Exception e) {
                            }
                        }
                    }
                }
            }

            //removendo a operacao selecionada
            triTempo.get(menorTempoIndiceY0).remove(0);
            triInicio.get(menorTempoIndiceY0).remove(0);
        }

        //--------------------------------------
        Log.i("guloso", "--------------------------------------");
        Log.i("guloso", "Total de iteracoes: "+(JOM));
        tvResult.append("Total de iteracoes: "+(JOM)+"\n");


    }

    private ArrayList<ArrayList<ArrayList<Integer>>> criaArrayTempo() {
        //array contendo o tempo de execução de cada operacao de cada máquina
        ArrayList<ArrayList<ArrayList<Integer>>> triTempo = new ArrayList<>();

        try{
            triTempo.add(new ArrayList<ArrayList<Integer>>());
            triTempo.add(new ArrayList<ArrayList<Integer>>());
            triTempo.add(new ArrayList<ArrayList<Integer>>());

            triTempo.get(0).add(new ArrayList<Integer>());
            triTempo.get(1).add(new ArrayList<Integer>());
            triTempo.get(2).add(new ArrayList<Integer>());

            triTempo.get(0).add(new ArrayList<Integer>());
            triTempo.get(1).add(new ArrayList<Integer>());
            triTempo.get(2).add(new ArrayList<Integer>());

            triTempo.get(0).add(new ArrayList<Integer>());
            triTempo.get(1).add(new ArrayList<Integer>());
            triTempo.get(2).add(new ArrayList<Integer>());
        }catch(Exception e){
            Log.i("guloso","tryTempo erro nas jobs/operacoes "+e);
        }
        try {

//J1
            //o1
            triTempo.get(0).get(0).add(4);//m1
            triTempo.get(0).get(0).add(0);//m2
            triTempo.get(0).get(0).add(0);//m3
            //o2
            triTempo.get(0).get(1).add(0);//m1
            triTempo.get(0).get(1).add(5);//m2
            triTempo.get(0).get(1).add(0);//m3
            //o3
            triTempo.get(0).get(2).add(0);//m1
            triTempo.get(0).get(2).add(0);//m2
            triTempo.get(0).get(2).add(10);//m3

//J2
            //o1
            triTempo.get(1).get(0).add(15);//m1
            triTempo.get(1).get(0).add(0);//m3
            triTempo.get(1).get(0).add(0);//m2
            //o2
            triTempo.get(1).get(1).add(0);//m1
            triTempo.get(1).get(1).add(20);//m2
            triTempo.get(1).get(1).add(0);//m3
            //o3
            triTempo.get(1).get(2).add(0);//m1
            triTempo.get(1).get(2).add(0);//m2
            triTempo.get(1).get(2).add(10);//m3

//J3
            //o1
            triTempo.get(2).get(0).add(0);//m1
            triTempo.get(2).get(0).add(5);//m3
            triTempo.get(2).get(0).add(0);//m2
            //o2
            triTempo.get(2).get(1).add(10);//m1
            triTempo.get(2).get(1).add(0);//m2
            triTempo.get(2).get(1).add(0);//m3
            //o3
            triTempo.get(2).get(2).add(0);//m1
            triTempo.get(2).get(2).add(0);//m2
            triTempo.get(2).get(2).add(2);//m3

            Log.i("guloso","Array de tempo criada automaticamente");
            //triTempo.get(0).get(0).set(0, 4);
            //triTempo.get(0).set(1, new).set(0, 4);
        }catch(Exception e){
            Log.i("guloso","tryTempo erro nas máquinas "+e);
        }
        return triTempo;
    }

    private void criaArrayInicio() {
        //array contendo o tempo inicial de cada operacao de cada máquina
        triInicio = new ArrayList<>();
        try{
            for(int i=0;i<dimX;i++) {//primeira dimensao (JOBS)
                triInicio.add(new ArrayList<ArrayList<Integer>>());

                for(int j=0;j<dimY;j++) {//segunda dimensao (Operacoes)
                    triInicio.get(i).add(new ArrayList<Integer>());

                    for(int k=0;k<dimZ;k++) {//segunda dimensao (Maquinas)
                        triInicio.get(i).get(j).add(0);//m1
                    }
                }
            }
            Log.i("guloso","Array de Inicio criada com sucesso");
        }catch(Exception e){
            Log.i("guloso","tryInicio erro nas jobs/operacoes "+e);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            //return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
