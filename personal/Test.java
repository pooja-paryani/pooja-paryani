import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.Comparator.*;
import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.toCollection;

public class Test {

    public static void main(String[] args){

        List<ProductA> p = new ArrayList<>();
        p.add(new ProductA("2","12","123","A","desc"));
        p.add(new ProductA("3","13","124","A","desc"));
        //p.add(new ProductA("1","14","125","B"));
        //p.add(new ProductA("1","14","128","B"));

        List<ProductA> t = new ArrayList<>();
        t.add(new ProductA("1","11","123","B",""));
        t.add(new ProductA("1","14","125","B",""));
        t.add(new ProductA("1","14","128","B",""));

        Set<ProductA> setOne = new HashSet<>(p);
        List<ProductA> listCommon = p.stream()
                .filter(e -> setOne.contains(e))
                .collect(Collectors.toList());

        //listCommon.stream().filter(distinctByKey(ProductA::getSKU)).forEach(x-> System.out.println(x.getSKU()));

        Set<ProductA> data = new HashSet<>();
        data.addAll(p);
        data.addAll(t);
        data.stream().filter(distinctByKey(ProductA::getSKU)).forEach(x-> System.out.println(x.getSKU()));






//********************************************************************************************
        /*p.sort(Comparator.comparing(a -> a.getSource()));

        List<ProductA> unique = p.stream()
                .collect(collectingAndThen(toCollection(() -> new TreeSet<>(comparing(ProductA::getBarcode))),
                        ArrayList::new));

        //p.stream().forEach(x-> System.out.println(x.getSource()));

        //Map<ProductA, Boolean> isDuplicate = p.stream().collect(Collectors.toMap())

        HashSet<Object> seen=new HashSet<>();
        Predicate<ProductA> condition = e -> e.getSource().equalsIgnoreCase("B");
        p.removeIf(e ->!seen.add(e.getBarcode())  );

        unique.stream().filter(distinctByKey(ProductA::getSKU)).forEach(x-> System.out.println(x.getSKU()));
*/

    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }


}
