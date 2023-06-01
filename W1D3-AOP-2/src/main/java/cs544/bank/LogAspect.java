package cs544.bank;

import cs544.bank.logging.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class LogAspect {
    @Autowired
    private Logger loggerAop;

    @After("within(cs544.bank.dao..*)")
    public void logAfterDao(JoinPoint joinpoint) {
        loggerAop.log("Method inside bank.dao package called (" + joinpoint.getSignature().getName() + ")");
    }

    @Around("within(cs544.bank.service..*)")
    public Object invoke(ProceedingJoinPoint call ) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start(call.getSignature().getName());
        Object retVal = call.proceed();
        sw.stop();

        long totaltime = sw.getLastTaskTimeMillis();
        Signature signature = call.getSignature();

        System.out.println("Time to execute the following call " + signature +" is "+ totaltime + " ms");
        return retVal;
    }

    @After("within(cs544.bank.jms..*)")
    public void logAfterJms(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        loggerAop.log("Jms message is sent: " + args[0]);
    }
}

