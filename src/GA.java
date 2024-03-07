import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class GA {
    private int n_genes,n_generation,n_population;
    private float r_crossover, r_mutation;
    private MainPaper main_rectangle;
    private List<Rectangle> rectangles;
    public GA(int n_genes,int n_generation,int n_population,float r_crossover,float r_mutation,
              MainPaper main_rectangle ,List<Rectangle> rectangles){
        this.n_genes = n_genes;
        this.n_generation = n_generation;
        this.n_population = n_population;
        this.r_crossover = r_crossover;
        this.r_mutation = r_mutation;
        this.main_rectangle = main_rectangle;
        this.rectangles = rectangles;
    }

    public void Start(){
        List<List<Integer>> population = generatePopulation(rectangles, n_population,n_genes);
        List<Integer> best = new ArrayList<>();
        double bestEval = findFitness(population.getFirst(), main_rectangle, rectangles);
        System.out.print(best + " ");
        System.out.println(bestEval);
        for (int gen = 0; gen < n_generation; gen++) {
            List<Double> fitness = new ArrayList<>();
            for (List<Integer> individual : population) {
                double individualFitness = findFitness(individual, main_rectangle, rectangles);
                fitness.add(individualFitness);
            }
            for (int i = 0; i < n_population; i++) {
                if (fitness.get(i) > bestEval) {
                    best = population.get(i);
                    bestEval = fitness.get(i);
                    System.out.printf(">%d, new best f(%s) = %.3f\n", gen, population.get(i).toString(), fitness.get(i));

                    MainPaper copyMainRectangle = main_rectangle.copy();
                    int[][] fitnessMatrix = copyMainRectangle.matrix;
                    cutting(best, fitnessMatrix);
                    for(int newBest=0 ;newBest < fitnessMatrix.length;newBest++) System.out.println(Arrays.toString(fitnessMatrix[newBest]));
                }
            }
            List<List<Integer>> selected  = selection(population,fitness,n_population);
            List<List<Integer>> children =  new ArrayList<>();
            for (int i = 0; i < n_population; i += 2) {
                List<Integer> p1 = selected.get(i);
                List<Integer> p2 = selected.get(i + 1);

                for (List<Integer> c : crossover(p1, p2, r_crossover)) {
                    mutation(c, r_mutation, rectangles.size());
                    children.add(new ArrayList<>(c));
                }
            }
            population = new ArrayList<>(children);
        }
    }
    private static void mutation(List<Integer> bitstring, double r_mut, int idx_limit) {
        Random random = new Random();
        for (int i = 0; i < bitstring.size(); i++) {
            // Check for a mutation
            if (random.nextDouble() < r_mut) {
                // Flip the bit
                bitstring.set(i,random.nextInt(idx_limit )) ;
            }
        }
    }
    private static <T> List<List<T>> crossover(List<T> p1, List<T> p2, double r_cross) {
        Random random = new Random();
        List<T> c1 = new ArrayList<>(p1);
        List<T> c2 = new ArrayList<>(p2);

        if (random.nextDouble() < r_cross) {
            int pt = random.nextInt(p1.size() - 1) + 1;

            c1 = new ArrayList<>(p1.subList(0, pt));
            c1.addAll(p2.subList(pt, p2.size()));

            c2 = new ArrayList<>(p2.subList(0, pt));
            c2.addAll(p1.subList(pt, p1.size()));
        }

        List<List<T>> result = new ArrayList<>();
        result.add(c1);
        result.add(c2);

        return result;
    }

    public static List<List<Integer>> generatePopulation(List<Rectangle> rectangles, int nPopulation, int nGenes) {
        Random random = new Random();
        List<List<Integer>> population = new ArrayList<>();

        for (int i = 0; i < nPopulation; i++) {
            List<Integer> individual = new ArrayList<>();
            for (int j = 0; j < nGenes; j++) {
                int randomIndex = random.nextInt(rectangles.size());
                individual.add(randomIndex);
            }
            population.add(individual);
        }

        return population;
    }


    public static List<List<Integer>> selection(List<List<Integer>> pop, List<Double> fitness,int nPopulation) {
        int k =3;
        Random random = new Random();

        List<List<Integer>> selectedPopulation = new ArrayList<>();
        for (int p = 0; p < nPopulation; p++) {
            int selectionIx = random.nextInt(pop.size());
            for (int i = 0; i < k - 1; i++) {
                int ix = random.nextInt(pop.size());
                // Check if better (perform a tournament)
                if (fitness.get(ix) > fitness.get(selectionIx)) {
                    selectionIx = ix;
                }
            }
            selectedPopulation.add(pop.get(selectionIx));
        }

        return selectedPopulation;
    }

    public float findFitness(List<Integer> individual, MainPaper mainRectangle, List<Rectangle> rectangles) {
        MainPaper ini =  mainRectangle.copy(); // Assuming Rectangle class has a copy constructor
        int[][] fitnessMatrix = ini.matrix;

        // Assuming Rectangle class has a method to get the matrix
        return cutting(individual, fitnessMatrix);
    }

    private float cutting(List<Integer> individual, int[][] fitnessMatrix) {
        float fitness = 0;

        for(Integer inv : individual){
            if(inv == 0) continue;
            int[] nearest_zero_index = findNearestZero(fitnessMatrix);
            if(nearest_zero_index == null){
                fitness -= rectangles.get(inv).row*rectangles.get(inv).col;
                continue;
            }
            if(!isFit(nearest_zero_index[0],nearest_zero_index[1],rectangles.get(inv).row,rectangles.get(inv).col,fitnessMatrix)){
                for(int i = 0;i< fitnessMatrix.length * fitnessMatrix[0].length; i++){
                    nearest_zero_index = findNextZero(fitnessMatrix,nearest_zero_index);
                    if (nearest_zero_index == null) break;
                    if(isFit(nearest_zero_index[0],nearest_zero_index[1],rectangles.get(inv).row,rectangles.get(inv).col,fitnessMatrix)){
                        break;
                    }
                }
                if(nearest_zero_index == null) {
                    fitness -= rectangles.get(inv).row*rectangles.get(inv).col;
                    continue;
                }
            }
            for (int row = nearest_zero_index[0]; row < nearest_zero_index[0] + rectangles.get(inv).row; row++) {
                for (int col = nearest_zero_index[1]; col <nearest_zero_index[1] + rectangles.get(inv).col; col++) {
                    fitnessMatrix[row][col] = rectangles.get(inv).index;
                }
            }
            fitness += rectangles.get(inv).row*rectangles.get(inv).col;
        }
        return fitness;
    }

    public static int[] findNextZero(int[][] matrix, int[] startIndex) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        int row = startIndex[0];
        int col = startIndex[1];

        int[] flattenedMatrix = Arrays.stream(matrix)
                .flatMapToInt(Arrays::stream)
                .toArray();

        int[] flattenedIndices = IntStream.range(0, flattenedMatrix.length)
                .filter(i -> flattenedMatrix[i] == 0)
                .toArray();

        for (int flattenedIndex : flattenedIndices) {
            int i = flattenedIndex / cols;
            int j = flattenedIndex % cols;

            if (i > row || (i == row && j > col)) {
                return new int[]{i, j};
            }
        }
        return null;
    }

    public boolean isFit(int startRow, int startCol, float height, float width, int[][] matrix) {
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if (startRow + row >= matrix.length || startCol + col >= matrix[0].length) {
                    return false;
                }
                if (matrix[startRow + row][startCol + col] != 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int[] findNearestZero(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == 0) {
                    return new int[]{i, j};
                }
            }
        }
        // If no zero is found, you may return a special value or handle it as needed.
        return null;
    }

}
