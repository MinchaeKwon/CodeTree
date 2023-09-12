/**
 * 격자 숫자 놀이
 * 삼성 SW 역량테스트 2019 상반기 오후 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/matrix-number-play/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 13.
 * 
 * 백준 17140 이차원 배열과 연산
 * https://www.acmicpc.net/problem/17140
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.PriorityQueue;
import java.util.StringTokenizer;

class Number implements Comparable<Number> {
    int n;
    int cnt; // 숫자 출현 빈도

    public Number(int n, int cnt) {
        this.n = n;
        this.cnt = cnt;
    }

    @Override
        public int compareTo(Number o) {
            if (this.cnt == o.cnt) {
                return this.n - o.n; // 출현 빈도 수가 같은 경우에는 숫자를 기준으로 오름차순 정렬
            }

            return this.cnt - o.cnt; // 출현 빈도 수를 기준으로 오름차순 정렬
        }
}

public class Main {

    static int r, c, k;
    static int[][] A;

    static int[] cntArr;
    static int[][] cal;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        r = Integer.parseInt(st.nextToken());
        c = Integer.parseInt(st.nextToken());
        k = Integer.parseInt(st.nextToken());

        A = new int[3][3]; // 초기에는 3x3 크기

        for (int i = 0; i < 3; i++) {
            st = new StringTokenizer(br.readLine());

            for (int j = 0; j < 3; j++) {
                A[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        int time = 0;

        while (true) {
            int rLen = A.length; // 행의 길이
            int cLen = A[0].length; // 열의 길이

            // 배열 크기가 r x c와 같거나 크고, A[r - 1][c - 1]값이 k가 된 경우
            if (r <= rLen && c <= cLen && A[r - 1][c - 1] == k) {
                System.out.println(time);
                break;
            }

            // 목표 숫자에 도달하는 것이 불가능하면 -1 출력
            if (time > 100) {
                System.out.println(-1);
                break;
            }

            if (rLen >= cLen) {
                calculateRow(); // 행 정렬
            } else {
                calculateCol(); // 열 정렬
            }

            time++;
        }
    }

    // 모든 행에 대하여 정렬 수행
    private static void calculateRow() {
        cal = new int[101][101];
        int maxLen = 0;

        for (int i = 0; i < A.length; i++) {
            cntArr = new int[101];
            
            for (int j = 0; j < A[i].length; j++) {
                cntArr[A[i][j]]++; // 각 숫자의 출현 빈도 횟수 저장
            }

            PriorityQueue<Number> pq = new PriorityQueue<>();

            // 0은 무시하기 때문에 1부터 시작
            for (int k = 1; k < cntArr.length; k++) {
                if (cntArr[k] > 0) {
                    pq.add(new Number(k, cntArr[k])); // 숫자와 출현 빈도 횟수 저장
                }
            }

            maxLen = Math.max(maxLen, pq.size());

            int idx = 0;

            while(!pq.isEmpty()) {
                Number cur = pq.poll();

                cal[i][idx] = cur.n;
                idx++;

                cal[i][idx] = cur.cnt;
                idx++;
            }
        }

        int nr = A.length;
        int nc = maxLen * 2;

        // 행이나 열의 길이가 100을 넘어가는 경우에는 처음 100개의 격자를 제외함
        if (nr > 100) {
            nr = 100;
        }

        if (nc > 100) {
            nc = 100;
        }

        A = new int[nr][nc]; // 배열 크기 갱신

        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                A[i][j] = cal[i][j]; // 배열 계산된 값을 원본 배열에 넣어줌
            }
        }
    }

    // 모든 열에 대하여 정렬 수행
    private static void calculateCol() {
        cal = new int[101][101];
        int maxLen = 0;

        for (int j = 0; j < A[0].length; j++) {
            cntArr = new int[101];
            
            for (int i = 0; i < A.length; i++) {
                cntArr[A[i][j]]++; // 각 숫자의 출현 빈도 횟수 저장
            }

            PriorityQueue<Number> pq = new PriorityQueue<>();

            // 0은 무시하기 때문에 1부터 시작
            for (int k = 1; k < cntArr.length; k++) {
                if (cntArr[k] > 0) {
                    pq.add(new Number(k, cntArr[k])); // 숫자와 출현 빈도 횟수 저장
                }
            }

            maxLen = Math.max(maxLen, pq.size());

            int idx = 0;

            while(!pq.isEmpty()) {
                Number cur = pq.poll();

                cal[idx][j] = cur.n;
                idx++;

                cal[idx][j] = cur.cnt;
                idx++;
            }
        }

        int nr = maxLen * 2;
        int nc = A[0].length;

        // 행이나 열의 길이가 100을 넘어가는 경우에는 처음 100개의 격자를 제외함
        if (nr > 100) {
            nr = 100;
        }

        if (nc > 100) {
            nc = 100;
        }

        A = new int[nr][nc]; // 배열 크기 갱신

        for (int i = 0; i < nr; i++) {
            for (int j = 0; j < nc; j++) {
                A[i][j] = cal[i][j]; // 배열 계산된 값을 원본 배열에 넣어줌
            }
        }
    }

}