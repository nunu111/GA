import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");
        MainPaper Main_paper =  new MainPaper(10,20);
        List<Rectangle> sub_paper = new ArrayList<>();
        sub_paper.add(new Rectangle(3,3,1,false));
        sub_paper.add(new Rectangle(2,3,2,false));
        int size = sub_paper.size();
        for (int i =0 ; i < size ; i++) {
            sub_paper.add(sub_paper.get(i).Rotate());
        }
//        sub_paper.add(new Rectangle(1,1,1));
        function f = new function();
        int n_generation = 500;
        int n_genes = f.genesGenerator(Main_paper,sub_paper);

        sub_paper.addFirst(new Rectangle(0,0,0,false));
        int n_population = 100;
        float r_crossover = 0.9F;
        float r_mutation = (float) (1.0 / (float)n_genes);

        GA ga = new GA(n_genes, n_generation, n_population, r_crossover, r_mutation,Main_paper ,sub_paper);
        ga.Start();
    }
}