import domain.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Properties;

public class HibernateUserRepository implements IUserRepository {
    public static SessionFactory sessionFactory = null;

    static void initialize() {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry ).buildMetadata().buildSessionFactory();
        }
        catch (Exception e) {
            System.out.println(sessionFactory);
            System.err.println("Exception "+e);
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    static void close(){
        if ( sessionFactory != null ) {
            sessionFactory.close();
        }

    }

    public HibernateUserRepository(Properties properties) {
        System.out.println("HibernateUserRepository() with properties " + properties);
    }


    @Override
    public boolean findOne(String un, String psswd)
    {
        System.out.println("USERNAME :" + un);
        System.out.println("PASSWORD : " + psswd);
        initialize();
        try(Session session = sessionFactory.openSession()) {
            try {
                Query query = session.createQuery("SELECT U from User U  WHERE U.username = :un and U.password = :psswd");
                query.setParameter("un", un);
                query.setParameter("psswd", psswd);
                User user = (User) query.uniqueResult();
                System.out.println(query.toString());
                System.out.println(user);
                return user != null;
            } catch (RuntimeException ex) {
                ex.printStackTrace();
            } finally {
                close();
            }
            return false;
        }
    }

    @Override
    public List<User> findAll() {
        initialize();
        try (Session session = sessionFactory.openSession()) {
            try {
                String queryString = "from User";
                List<User> users =
                        session.createQuery(queryString, User.class)
                                .list();
                System.out.println(users.size() + " user(s) found:");
                return users;
            } catch (RuntimeException ex) {
                return null;
            } finally {
                close();
            }
        }
    }

    @Override
    public void disconnect() {

    }
}
