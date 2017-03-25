package logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.*;

@Aspect
public class StatsAspect {

    public static final int TOP_INNER_CALLS = 5;

    public Stack<ProceedingJoinPoint> stack = new Stack<>();
    public Map<ProceedingJoinPoint, ArrayList<JoinPointWithTimeRange>> calls = new HashMap<>();
    public Map<String, Integer> counters = new HashMap<>();
    public Map<String, Long> timers = new HashMap<>();
    public Map<String, Long> maxTimers = new HashMap<>();

    @Around("execution(public * ru.ifmo.sd.aspectj.*.*(..))")
    public Object employeeAroundAdvice(ProceedingJoinPoint jp) {
        calls.put(jp, new ArrayList<>());

        ProceedingJoinPoint parent = (!stack.isEmpty() ? stack.peek() : null);
        JoinPointWithTimeRange current = new JoinPointWithTimeRange(jp);
        if (parent != null) {
            if (calls.get(parent).size() == 0) {
                System.out.println(); // print \n
            }
            calls.get(parent).add(current);
        }

        String shift = getShift(stack.size());
        System.out.print(shift + jp.getSignature().getName()+ Arrays.toString(jp.getArgs()));

        stack.push(jp);

        current.setStart(System.currentTimeMillis());
        try {
            jp.proceed();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        current.setEnd(System.currentTimeMillis());

        stack.pop();

        // counter & timers

        Integer counter = counters.get(jp.getSignature().getName());
        if (counter == null) {
            counter = 0;
        }
        counters.put(jp.getSignature().getName(), counter + 1);

        Long timer = timers.get(jp.getSignature().getName());
        if (timer == null) {
            timer = 0L;
        }
        timers.put(jp.getSignature().getName(), timer + (current.getEnd() - current.getStart()));

        Long maxTimer = maxTimers.get(jp.getSignature().getName());
        if (maxTimer == null) {
            maxTimer = 0L;
        }
        maxTimers.put(jp.getSignature().getName(), Math.max(maxTimer, (current.getEnd() - current.getStart())));

        //print statistics

        int innerCalls = calls.get(jp).size();

        if (innerCalls > 0) {
            char c = ExtendedAscii.getAscii(194);
            System.out.println(shift + c + "-----");

            char sigma = ExtendedAscii.getAscii(227);
            System.out.println(shift + c + " " + sigma + " time: " + (current.getEnd() - current.getStart()) + " ms.");

            if (innerCalls < TOP_INNER_CALLS) {
                System.out.println(shift + c + " all (" + innerCalls + ") inner calls:");
            } else {
                System.out.println(shift + c + " top " + TOP_INNER_CALLS + " (of " + innerCalls + ") inner calls:");
            }
            calls.get(jp).sort(new JoinPointComparator());
            String innerShift = getShift(stack.size() + 1);
            for(int i = 0; i < Math.min(TOP_INNER_CALLS, calls.get(jp).size()); i++) {
                System.out.println(innerShift + "- " + calls.get(jp).get(i).toString());
            }

            char corner = ExtendedAscii.getAscii(191);
            System.out.println(shift + corner + "-----");
        } else {
            System.out.println(" - " + (current.getEnd() - current.getStart()) + " ms.");
        }

        // total results

        if (parent == null) {
            char sigma = ExtendedAscii.getAscii(227);
            counters.forEach((k, v) -> {
                System.out.println(k + " - " +
                        v + " times " +
                        "(" +
                        sigma + ": " + timers.get(k) + " ms, " +
                        "avg: " + ((1.0 * timers.get(k)) / v) + " ms, " +
                        "max: " + maxTimers.get(k) + " ms" +
                        ")"
                );
            });
        }

        return null;
    }

    private String getShift(int level) {
        StringBuilder builder = new StringBuilder();
        for(int i = 0; i < level; i++) {
            builder.append("|  ");
        }
        return builder.toString();
    }
}