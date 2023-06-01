package cs544;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;


@Aspect
@Component
public class LogAspect {
    private static Logger logger = LogManager.getLogger(LogAspect.class.getName());

    @After("execution(* cs544.EmailSender.sendEmail(..))")
    public void logAfter(JoinPoint joinpoint) {
        Object[] args = joinpoint.getArgs();
        Object target = joinpoint.getTarget();

        logger.warn("method= " + joinpoint.getSignature().getName() + " address=" + args[0] + " message= " + args[1]
        +" outgoing mail server = " + ((EmailSender)target).getOutgoingMailServer());
    }

    @Around("execution(* cs544.CustomerDAO.save(..))")
    public Object invoke(ProceedingJoinPoint call ) throws Throwable {
        StopWatch sw = new StopWatch();
        sw.start(call.getSignature().getName());
        Object retVal = call.proceed();
        sw.stop();

        long totaltime = sw.getLastTaskTimeMillis();

        System.out.println("Time to execute save = "+ totaltime + " ms");
        return retVal;
    }
}
