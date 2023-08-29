/**
 * 바이러스 검사
 * 삼성 SW 역량테스트 2015 하반기 1번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/virus-detector/description?page=1&pageSize=20
 * 
 * @author minchae
 * @date 2023. 8. 30.
 * 
 * 백준 13458 시험 감독
 * https://www.acmicpc.net/problem/13458
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int n = Integer.parseInt(br.readLine()); // 식당의 수

        int[] customer = new int[n]; // 각 식당에 있는 고객의 수

        StringTokenizer st = new StringTokenizer(br.readLine());
        for (int i = 0; i < n; i++) {
            customer[i] = Integer.parseInt(st.nextToken());
        }

        st = new StringTokenizer(br.readLine());
        int leader = Integer.parseInt(st.nextToken()); // 검사팀장이 검사할 수 있는 최대 고객 수
        int member = Integer.parseInt(st.nextToken()); // 검사팀원이 검사할 수 있는 최대 고객 수

        long result = n; // 각 가게에 팀장 한명은 무조건 있어야하기 때문에 n 넣어줌

        for (int i = 0; i < n; i++) {
            customer[i] -= leader; // 팀장이 검사할 수 있는 고개의 수 빼기

            if (customer[i] > 0) {
                result += customer[i] / member; // 팀원이 검사할 수 있는 고객의 수를 나눈 몫을 더함

                // 나머지가 있을 경우에는 한명 더 추가
                if (customer[i] % member > 0) {
                    result++;
                }
            }
        }

        System.out.println(result);
    }
}