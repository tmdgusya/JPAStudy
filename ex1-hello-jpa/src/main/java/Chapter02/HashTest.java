package Chapter02;

public class HashTest {

    static class A{
        A(){}

        B b;
        int age = 20;
    }

    static class B{
        B(){}

        A a;
        int age = 10;

    }

    public static void main(String[] args) {
        B b = new B();
        A a = new A();
        b.a = a;
        a.b = b;
        int A_HASH = System.identityHashCode(a);
        int B_HASH = System.identityHashCode(b);
        int A_AGE = a.age;
        int B_AGE = b.age;

        int OBJB_A_HASH = System.identityHashCode(b.a);
        int OBJA_B_HASH = System.identityHashCode(a.b);

        System.out.println("A_HASH = " + A_HASH);
        System.out.println("B_HASH = " + B_HASH);
        System.out.println("OBJB_A_HASH = " + OBJB_A_HASH);
        System.out.println("OBJA_B_HASH = " + OBJA_B_HASH);

        System.out.println("b.age = " + b.age);
        System.out.println("a.age = " + a.age);

        b.a.age = 30;
        a.b.age = 40;

        System.out.println("b.age = " + b.age);
        System.out.println("a.age = " + a.age);


    }
}
