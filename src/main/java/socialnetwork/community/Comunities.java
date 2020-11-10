package socialnetwork.community;

import socialnetwork.domain.Prietenie;
import socialnetwork.domain.Tuple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Comunities {
    //lista de adiacenta
    private ArrayList<ArrayList<Long>> adiacentaList = new ArrayList<ArrayList<Long>>();
    //pt DFS vector vizitat
    private Map<Long, Long> vizitat = new HashMap<>();
    //numarul maxim de prieteni
    private int V = 100;
    private Iterable<Prietenie> listFriendship;


    public Comunities(Iterable<Prietenie> listFriendship) {
        this.listFriendship = listFriendship;
        for(int i = 0; i <V; i++){
            adiacentaList.add(new ArrayList<Long>());
        }

        listFriendship.forEach(friendship ->{
            adiacentaList.get(friendship.getId().getLeft().intValue()).add(friendship.getId().getRight());
            adiacentaList.get(friendship.getId().getRight().intValue()).add(friendship.getId().getLeft());
            this.resetVizitat();
        });

        //printare
//        for(int i = 0; i< V; i++)
//            System.out.println(adiacentaList.get(i));
    }

    /**
     * graph traversal through dfs
     * @param vertex
     * @return number of vertices visited
     */
    private int DFS(Long vertex){
        vizitat.put(vertex, 1l);
        int nrPrieteni = 1;
        //System.out.println("vertex "+vertex+" vizitat");
        for(Long child : adiacentaList.get(vertex.intValue())){
            if(vizitat.get(child)==0)
                nrPrieteni = nrPrieteni + DFS(child);
        }
        return nrPrieteni;
    }

    /**
     * resets the values in the vector to 0
     */
    private void resetVizitat(){
        listFriendship.forEach(friendship ->{
            vizitat.put(friendship.getId().getLeft(),0l);
            vizitat.put(friendship.getId().getRight(),0l);
        });
    }

    /**
     * displays the number of communities - the related components in the graph
     */
    //afisare numar comunitati - componente conexe
    public void printNumberofComunities(){
        int numarComunitati =0;
        for(Long vertex : vizitat.keySet()){
            if(vizitat.get(vertex) == 0){
                DFS(vertex);
                numarComunitati++;
            }
        }
        System.out.println("The number of comunities is eqaul to: "+numarComunitati);
    }

    /**
     *display the most sociable community - the component connected with the most vertices
     */
    //afisare cea mai sociabila comunitate -cel mai lung drum
    public void printTheMostSociableCommunity(){
        int nrMaxPeople = 0;
        long representMaxCommunity = 0;
        for(Long vertex : vizitat.keySet()){
            if(vizitat.get(vertex) == 0){
                int nrPeople = DFS(vertex);
                if(nrMaxPeople < nrPeople){
                    nrMaxPeople = nrPeople;
                    representMaxCommunity = vertex;
                }
            }
        }

        resetVizitat();
        DFS(representMaxCommunity);
        System.out.println("The most soaciable community has "+nrMaxPeople+" members: ");
        for(Long vertex: vizitat.keySet()){
            if(vizitat.get(vertex) == 1){
                System.out.println(vertex+" ");
            }
        }
        System.out.println();
    }
}
