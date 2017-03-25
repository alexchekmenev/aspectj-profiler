package logging;

import java.util.Comparator;

public class JoinPointComparator implements Comparator<JoinPointWithTimeRange> {

    @Override
    public int compare(JoinPointWithTimeRange o1, JoinPointWithTimeRange o2) {
        long l1 = o1.getEnd() - o1.getStart();
        long l2 = o2.getEnd() - o2.getStart();
        int cmp = 0;
        if (l1 < l2) {
            cmp = -1;
        } else if (l1 > l2) {
            cmp = 1;
        }
        return -cmp;
    }

    @Override
    public boolean equals(Object obj) {
        return false;
    }
}
