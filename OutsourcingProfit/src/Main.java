/**
 * 외주 수익 최대화하기
 * 삼성 SW 역량테스트 2017 상반기 오전 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/max-of-outsourcing-profit/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 26.
 * 
 * 백준 14501 퇴사
 * https://www.acmicpc.net/problem/14501
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int n;
    static int[] t, p;

    static int result = 0;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());
        t = new int[n]; // 작업 기간
        p = new int[n]; // 수익

        for (int i = 0; i < n; i++) {
            StringTokenizer st = new StringTokenizer(br.readLine());

            t[i] = Integer.parseInt(st.nextToken());
            p[i] = Integer.parseInt(st.nextToken());
        }

        solve1(0, 0);
        System.out.println(result);

        solve2();
    }

    // 재귀 사용 -> 시간초과 발생할 수 있음
    private static void solve1(int idx, int price) {
        // 휴가 기간에 딱 맞춰서 끝나는 경우에 최댓값 갱신
        if (idx == n) {
            result = Math.max(result, price);
            return;
        }

        // 휴가 기간을 넘어가면 종료
        if (idx > n) {
            return;
        }

        solve1(idx + t[idx], price + p[idx]); // 작업을 하는 경우 -> 인덱스에 해당하는 작업 기간과 수익 증가시킴
        solve1(idx + 1, price); // 작업을 하지 않는 경우 날짜만 하루 증가, 수익은 그대로
    }

    // dp 사용
    private static void solve2() {
        int[] dp = new int[n + 1];

        for (int i = 0; i < n; i++) {
            if (i + t[i] <= n) {
                dp[i + t[i]] = Math.max(dp[i + t[i]], dp[i] + p[i]);
            }

            dp[i + 1] = Math.max(dp[i + 1], dp[i]);
        }

        System.out.println(dp[n]);
    }
}