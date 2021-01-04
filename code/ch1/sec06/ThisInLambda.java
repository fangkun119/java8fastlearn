/**
* 变量作用域：
* 1. Lambda表达式可以捕获闭合作用域的变量值，并且使用这些值
* 2. Lambda表达式强制约束，不可以修改这些所使用的局部变量
*    是因为Lambda访问这些变量的原理、类似于变量值拷贝、为了避免两个值不一致问题的发生（特别是多线程环境），增加了这个约束
* 3. 即便是增加了不可修改约束，仍然不能完全避免“不一致”问题的发生
*    (1) 类成员变量、静态变量不受约束（只约束局部变量）
*    (2) 如果变量是一个对象，不可修改约束只能保证这个变量不指向其他对象，不能阻挡对象内部的修改
*    彻底的解决方法、需要使用stream
* 4. Lambda表达式的方法体、与嵌套代码块有相同的作用域，适用同样的命名和屏蔽规则
* 5. Lambda表达式中使用this关键字时，会引用创建该表达式的方法的this参数（而不是执行该表达式的方法的this参数）
*/

public class ThisInLambda {
   public static void main(String[] args) {

      // Lambda表达式的方法体、与嵌套代码块有相同的作用域，适用同样的命名和屏蔽规则

      // Path first = Paths.get("/usr/bin");
      // Uncomment to see error "variable first is already defined"
      // in the lambda expression below
      ThisInLambda app = new ThisInLambda();
      app.doWork();
   }

   public void doWork() {      
      // Lambda表达式中使用this关键字时，会引用创建该表达式的方法的this参数（而不是执行该表达式的方法的this参数）

      Runnable runner = () -> { System.out.println(this.toString()); };
      runner.run();
      // Prints Application@... since this refers to an Application object, not runner Object
   }
}
