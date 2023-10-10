package solutions_01;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class Solutions_01_Introduction {
    
    private static final Random gen = new Random();
    
    public static <E> void reduceTo(List<E> ls, int newSize) {
        if(newSize < ls.size())
            ls.subList(newSize, ls.size()).clear();
    }

    // Ne fait rien si idx hors limites et renvoie false
    public static boolean suppAtPos(List<Integer> ls, int pos) {
        if (pos < 0 || pos >= ls.size()) {
            return false;
        }
        for (int k = pos + 1; k < ls.size(); ++k) {
            ls.set(k - 1, ls.get(k));
        }
        reduceTo(ls, ls.size() - 1); // Rétrécit de 1.
        return true;
    }

    // Algo de complexité O(n^2)
    public static void suppAllVal1(List<Integer> ls, int val) {
        int pos = 0;
        while(pos < ls.size()) {
            int x = ls.get(pos);
            if (x == val) 
                suppAtPos(ls, pos);
            else
                ++pos;
        }
    }
    
    // Algo de complexité O(n)
    public static void suppAllVal2(List<Integer> ls, int val) {
        int dest = 0;
        for (int orig = 0; orig < ls.size(); ++orig) {
            int x = ls.get(orig);
            if (x != val) {
                ls.set(dest, x);
                ++dest;
            }
        }
        reduceTo(ls, dest);
    }
        
    // Une liste contenant ENVIRON p% d'apparition de val
    public static List<Integer> randList(int size, int p, int val) {
        List<Integer> ls = new ArrayList<>(size);
        for(int k = 0; k < size; ++k) {
            if(gen.nextInt(100) < p)
                ls.add(val);
            else
                ls.add(1 + gen.nextInt(2 * size));
        }
        return ls;
    }
    

    // Le nb de millisecondes de l'algo suppAllVal pour supprimer val de la liste ls
    public static int timeAlgo(BiConsumer<List<Integer>, Integer> suppAllVal, List<Integer> ls, int val) {
        long deb = System.currentTimeMillis();
        suppAllVal.accept(ls, val);
        return (int) (System.currentTimeMillis() - deb);

    }

    // Le nb de millisecondes pour exécuter NB_ESSAIS fois l'algo de suppression suppAllVal sur la liste ls
    public static int timeAlgo(BiConsumer<List<Integer>, Integer> suppAllVal, List<Integer> ls, final int NB_ESSAIS, int val) {
        int time = 0;
        for(int i = 0; i < NB_ESSAIS; ++i) {
            List<Integer> copy = new ArrayList<>(ls);
            time += timeAlgo(suppAllVal, copy, val);
        }
        return time / NB_ESSAIS;
    }

    // Le nb de millisecondes pour exécuter NB_ESSAIS fois l'algo de suppression suppAllVal
    // sur des listes de taille size contenant environ 50% d'apparitions de la valeur (0) à supprimer
    public static int timeAlgo(BiConsumer<List<Integer>, Integer> suppAllVal, int size, final int NB_ESSAIS) {
        final int VAL = 0;
        return timeAlgo(suppAllVal, randList(size, 50, VAL), NB_ESSAIS, 0);
    }

    // Une paire de listes (tailles, temps) pour NB_ESSAIS applications de supp_fct
    // sur des listes de tailles croissantes
    public static Pair<List<Integer>, List<Integer>>
    times(BiConsumer<List<Integer>, Integer> suppAllVal, int size_min, final int NB_ESSAIS) {
        List<Integer> ls_size = new ArrayList<>();
        List<Integer> ls_time = new ArrayList<>();
        int size = size_min;
        for (int delta : List.of(10, 5, 2)) {
            for(int cpt = 0; cpt < 10; ++cpt) {
                ls_size.add(size);
                ls_time.add(timeAlgo(suppAllVal, size, NB_ESSAIS));
                size += size_min / delta;
            }
        }
        return Pair.of(ls_size, ls_time);
    }

    static void print(Pair<List<Integer>, List<Integer>> ts) {
        for(int i = 0; i < ts.first.size(); ++i) {
            System.out.println("Taille : " + ts.first.get(i) + " - Temps : " + ts.second.get(i));
        }
    }

    // Ajoute une ligne avec les entiers de ls dans fw
    static void saveList(List<Integer> ls, FileWriter fw) throws IOException {
        for(int x : ls)
            fw.write(x + " ");
        fw.write("\n");

    }

    // Ecrit les deux lignes d'entiers des deux listes de ts dans le fichier filename
    static void saveToFile(Pair<List<Integer>, List<Integer>> ts, String fileName) {
        print(ts);
        try(FileWriter fw = new FileWriter(fileName)) {
            saveList(ts.first, fw);
            saveList(ts.second, fw);
       }
        catch(IOException e) {}
    }

    // ATTENTION
    // Problèmes de chronomètre induit pas le JIT (Just In Time compiler)
    // Utiliser l'option Xint de la JVM
    public static void main(String[] args) {
        
        List<Integer> ls = new ArrayList<>(List.of(4, 2, 5, 4, 4, 4, 8, 4, 2, 3));
        System.out.println("Exercice 1 Avant suppression        : " + ls);
        suppAtPos(ls, 2);
        System.out.println("Après suppression (pos = 2)         : " + ls + "\n");
        
        ls = new ArrayList<>(List.of(4, 2, 5, 4, 4, 4, 8, 4, 2, 3));
        System.out.println("Exercice 2 Avant suppression        : " + ls);
        suppAllVal1(ls, 4);
        System.out.println("Après suppression (algo 1, val = 4) : " + ls + "\n");
        
        ls = new ArrayList<>(List.of(4, 2, 5, 4, 4, 4, 8, 4, 2, 3));
        System.out.println("Exercice 2 (bis) Avant suppression  : " + ls);
        suppAllVal2(ls, 4);
        System.out.println("Après suppression (algo 2, val = 4) : " + ls + "\n");

        saveToFile(times(Solutions_01_Introduction::suppAllVal1, 500, 2), "Times1.txt");
        saveToFile(times(Solutions_01_Introduction::suppAllVal2, 2000, 5), "Times2.txt");
        try {
            Runtime.getRuntime().exec("cmd /c \"python ShowData.py 1 2\" > test.txt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

class Pair<F, S> {

    public final F first;
    public final S second;

    private Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }
}

