package mum.app;

import mum.model.Order;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import mum.model.TeaEntity;


/**
 *
 * @author Tina
 */
@Named
@SessionScoped
public class TeaBean implements Serializable {
    @EJB //this annotation causes the container to inject this dependency
    private mum.db.TeaEntityFacade ejbTeaFacade; 
    private List<TeaEntity> teaList;
    private List<Order> orderList;
    
    public TeaBean() {
        /*
        teaList = new ArrayList<TeaEntity>(Arrays.asList(
                new TeaEntity("PU-ERH Tea", 20, 0),
                new TeaEntity("Green Tea", 10, 0),
                new TeaEntity("Black Tea", 15, 0),
                new TeaEntity("OOLONG", 12, 0)));*/
        orderList = new ArrayList<Order>();
        
    }
    @PostConstruct  //this annotation causes this method to run after the constructor completes
    public void init() { //create a few tea products, place in database, and load into the teaEntities list
       TeaEntity rose = new TeaEntity("rose tea", 5.95, 0);
       TeaEntity earl = new TeaEntity("Earl Grey tea", 4.50, 0);
       TeaEntity raja = new TeaEntity("Rajas Cup", 15.95, 0);

       ejbTeaFacade.create(rose);

       ejbTeaFacade.create(earl);

       ejbTeaFacade.create(raja);
                

       List<TeaEntity> teaEntities = ejbTeaFacade.findAll();

       teaList = new ArrayList<TeaEntity>();
       for (TeaEntity teaEnt: teaEntities) {
           teaList.add(teaEnt);
       }; 

    }  
    public List<TeaEntity> getTeaList() {
        return teaList;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * this is a jsf action method
     * sideeffects:  creates orders (i.e., order line items) and stores in the orderList
     * @return 
     */
    public String order() {
        for (int i = 0; i < teaList.size(); i++) {
            TeaEntity t = teaList.get(i);
            if (t.getQuantity()!= 0) {
                Order order = new Order();
                order.setName(t.getName());
                order.setPrice(t.getPrice());
                order.setAmount(t.getQuantity());
                order.setSum(t.getPrice() * t.getQuantity());
                orderList.add(order);
                t.setQuantity(0);
            }
        }
        return "purchase?faces-redirect=true";
    }

    public double getTotalPrice() {
        double total = 0.0;
        for (Order order : orderList) {
            total += order.getSum();
        }
        return total;
    }

    /**
     * jsf action method to clear the order and issue a redirect to go to the index page
     * @return 
     */
    public String clear() {
        orderList.clear();
        return "index?faces-redirect=true";
    }
}
