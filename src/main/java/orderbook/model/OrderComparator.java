package orderbook.model;

import java.util.Comparator;

public class OrderComparator implements Comparator<Order> {

    //-1 for Bid comparer
    //1 for Ask comparer
    private int _priceComparisonCoeff;

    OrderComparator(int priceComparisonCoeff)
    {
        _priceComparisonCoeff = priceComparisonCoeff;
    }

    @Override
    public int compare(Order x, Order y) {

        //two limit orders
        if (x.getPrice()!= y.getPrice())
        {
            return _priceComparisonCoeff * x.getPrice().compareTo(y.getPrice());
        }

        if (x.getTime() != y.getTime())
        {
            return x.getTime().compareTo(y.getTime());
        }


        //they have the same characteristics. not necessary same ID
        //not good because we are not supposed to have two equivalent orders in the orderbook
        return 0;
    }


    public static OrderComparator DescBidComparer()
    {
        return new OrderComparator(-1);
    }

    public static OrderComparator DescAskComparer()
    {
        return new OrderComparator(1);
    }
}
