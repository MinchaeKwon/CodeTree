/**
 * 연산자 배치하기
 * 삼성 SW 역량테스트 2017 하반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/arrange-operator/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 24.
 * 
 * 백준 14888 연산자 끼워넣기
 * https://www.acmicpc.net/problem/14888
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    
    static int n;
    static int[] num;
    static int[] operator = new int[3]; // 덧셈, 뺄셈, 곱셈의 개수 저장

    static int min = Integer.MAX_VALUE;
    static int max = Integer.MIN_VALUE;

    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        n = Integer.parseInt(br.readLine());

        num = new int[n];

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            num[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        for (int i = 0; i < 3; i++) {
            operator[i] = Integer.parseInt(st.nextToken());
        }
		
        solve(num[0], 1);

        System.out.println(min + " " + max);
    }

    // 백트래킹 이용
    private static void solve(int result, int depth) {
        if (depth == n) {
            min = Math.min(min, result);
            max = Math.max(max, result);

            return;
        }

        for (int i = 0; i < 3; i++) {
            // 연산자가 있을 경우
            if (operator[i] > 0) {
                operator[i]--;

                switch (i) {
                    case 0: // 덧셈
                        solve(result + num[depth], depth + 1);
                        break;

                    case 1: // 뺄셈
                        solve(result - num[depth], depth + 1);
                        break;

                    case 2: // 곱셈
                        solve(result * num[depth], depth + 1);
                        break;
                }

                operator[i]++; // 연산자 개수 원복
            }
        }
    }

}