
import java.util.*;


/**
 * Esta clase guarda una agenda con los festivales programados
 * en una serie de meses
 *
 * La agenda guardalos festivales en una colecci�n map
 * La clave del map es el mes (un enumerado festivales.modelo.Mes)
 * Cada mes tiene asociados en una colecci�n ArrayList
 * los festivales  de ese mes
 *
 * Solo aparecen los meses que incluyen alg�n festival
 *
 * Las claves se recuperan en orden alfab�ico
 *
 */
public class AgendaFestivales {
    private TreeMap<Mes, ArrayList<Festival>> agenda;
    
    public AgendaFestivales() {
        this.agenda = new TreeMap<>();
    }

    /**
     * a�ade un nuevo festival a la agenda
     *
     * Si la clave (el mes en el que se celebra el festival)
     * no existe en la agenda se crear� una nueva entrada
     * con dicha clave y la colecci�n formada por ese �nico festival
     *
     * Si la clave (el mes) ya existe se a�ade el nuevo festival
     * a la lista de festivales que ya existe ese ms
     * insert�ndolo de forma que quede ordenado por nombre de festival.
     * Para este segundo caso usa el m�todo de ayuda
     * obtenerPosicionDeInsercion()
     *
     */
    public void addFestival(Festival festival) {
        Mes mes = festival.getMes();
        if(!agenda.containsKey(mes)){
            agenda.put(mes, new ArrayList<>());
        }
        ArrayList<Festival> festivales = agenda.get(mes);
        int posicion = obtenerPosicionDeInsercion(festivales, festival);
        festivales.add(posicion, festival);
        
        
    }

    /**
     *
     * @param festivales una lista de festivales
     * @param festival
     * @return la posici�n en la que deber�a ir el nuevo festival
     * de forma que la lista quedase ordenada por nombre
     */
    private int obtenerPosicionDeInsercion(ArrayList<Festival> festivales, Festival festival) {
       int posicion = 0;
       for(Festival miFestival : festivales){
           if(miFestival.getNombre().compareTo(festival.getNombre()) > 0){
               break;
           }
           posicion++;
       }
        
        return posicion;
        
    }

    /**
     * Representaci�n textual del festival
     * De forma eficiente
     *  Usa el conjunto de entradas para recorrer el map
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Mes, ArrayList<Festival>> entry : agenda.entrySet()){
            sb.append(entry.getKey() + " : ");
            for(Festival miFestival : entry.getValue()){
                sb.append(miFestival.getNombre() + ", ");
            }
            sb.append("\n");

        }
        
        return sb.toString();
    }

    /**
     *
     * @param mes el mes a considerar
     * @return la cantidad de festivales que hay en ese mes
     * Si el mes no existe se devuelve -1
     */
    public int festivalesEnMes(Mes mes) {
       if(agenda.containsKey(mes)){
           return agenda.get(mes).size();
       }
        
        return 0;
    }

    /**
     * Se trata de agrupar todos los festivales de la agenda
     * por estilo.
     * Cada estilo que aparece en la agenda tiene asociada una colecci�n
     * que es el conjunto de nombres de festivales que pertenecen a ese estilo
     * Importa el orden de los nombres en el conjunto
     *
     * Identifica el tipo exacto del valor de retorno
     */
    public Map<Estilo, TreeSet<String>> festivalesPorEstilo() {
        Map<Estilo, TreeSet<String>> festivalesPorEstilo = new TreeMap<>();
        for (Mes mes : agenda.keySet()) {
            ArrayList<Festival> festivalesMes = agenda.get(mes);
            for (Festival festival : festivalesMes) {
                for (Estilo estilo : festival.getEstilos()) {
                    if (!festivalesPorEstilo.containsKey(estilo)) {
                        festivalesPorEstilo.put(estilo, new TreeSet<>());
                    }
                    festivalesPorEstilo.get(estilo).add(festival.getNombre());
                }
            }
        }
        return festivalesPorEstilo;
    }

    /**
     * Se cancelan todos los festivales organizados en alguno de los
     * lugares que indica el conjunto en el mes indicado. Los festivales
     * concluidos o que no empezados no se tienen en cuenta
     * Hay que borrarlos de la agenda
     * Si el mes no existe se devuelve -1
     *
     * Si al borrar de un mes los festivales el mes queda con 0 festivales
     * se borra la entrada completa del map
     */
    public int cancelarFestivales(HashSet<String> lugares, Mes mes) {
       if(!agenda.containsKey(mes)){
           return -1;
       }
       ArrayList<Festival> festivales = agenda.get(mes);
       int tamanyoInicial = festivales.size();
       Iterator<Festival> it = festivales.iterator();
       while (it.hasNext()){
           Festival festival = it.next();
           if(!festival.haConcluido() && lugares.contains(festival.getLugar())){
               it.remove();
           }
       }
       if(festivales.isEmpty()){
           agenda.remove(mes);
       }

        
        return tamanyoInicial - festivales.size();
    }
}
