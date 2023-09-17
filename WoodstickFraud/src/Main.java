/**
 * 윷놀이 사기단
 * 삼성 SW 역량테스트 2019 하반기 오후 2번 문제
 * https://www.codetree.ai/training-field/frequent-problems/problems/woodstick-fraud/description?page=2&pageSize=20
 * 
 * @author minchae
 * @date 2023. 9. 17.
 * 
 * 백준 17825 주사위 윷놀이
 * https://www.acmicpc.net/problem/17825
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {

    static int[] map = {
        0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20, 22, 24, 26, 28, 30, 32, 34, 36, 38, 40, 0, // 0 ~ 21
        10, 13, 16, 19, 25, 30, 35, 40, 0, // 22 ~ 30
        20, 22, 24, 25, 30, 35, 40, 0, // 31 ~ 38
        30, 28, 27, 26, 25, 30, 35, 40, 0 // 39 ~ 47
    };

    static int[] move = new int[10]; // 이동 칸 수
    static int[] order = new int[10]; // 말 이동순서 저장

    static boolean[] visited; // 특정 칸에 말이 있는지 확인
    static int result = 0;

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());

        for (int i = 0; i < 10; i++) {
            move[i] = Integer.parseInt(st.nextToken());
        }

        permulation(0);
        System.out.println(result);
    }

    // 말 순서 정하기 -> 중복순열 이용
    private static void permulation(int depth) {
        if (depth == 10) {
            moveHorse();
            return;
        }

        for (int i = 0; i < 4; i++) {
            order[depth] = i;
            permulation(depth + 1);
        }
    }

    // 말 이동시키기
    private static void moveHorse() {
        visited = new boolean[map.length];

        int[] horse = new int[4]; // 현재 말들의 위치 저장
        int score = 0; // 현재까지의 점수 저장

        for (int i = 0; i < 10; i++) {
            int nowCnt = move[i]; // 현재 이동할 칸 수
            int num = order[i]; // 현재 이동할 말

            // 해당 말이 도착 칸에 도착했는지 확인 (도착칸에 도착하지 않은 말을 골라 원하는 이동횟수만큼 이동할 수 있기 때문)
            if (horse[num] == 21 || horse[num] == 30 || horse[num] == 38 || horse[num] == 47) {
                return; // 이미 도착한 경우에는 종료
            }

            int next = findNextPoint(horse[num], nowCnt); // 말이 이동한 위치 구함

            // 말을 이동했는데 도착칸에 도달한 경우
            if (next == 21 || next == 30 || next == 38 || next == 47) {
                // 도착점은 true 처리 해줄 필요 없음 (여러 말이 있을 수 있음)
                setVisited(horse[num], false);
                horse[num] = next;

                continue; // 다음 말 이동시킴
            }

            // 이미 말이 있는 경우 종료 (시작칸과 도착칸을 제외하고 말들을 겹쳐서 올릴 수 없음)
            if (visited[next]) {
                return;
            }

            // 이동할 수 있는 칸이면 방문처리, 말 위치 갱신, 점수 추가
            setVisited(horse[num], false); // 현재 말이 있는 곳은 false
            setVisited(next, true); // 말이 이동하는 곳은 true

            horse[num] = next;
            score += map[horse[num]];
        }

        result = Math.max(result, score); // 최댓값 갱신
    }

    // 말 이동시킴
    private static int findNextPoint(int idx, int cnt) {
        int next = idx + cnt;

        // 도착칸에 도달하거나 넘어갈 경우 인덱스가 꼬이는걸 방지하기 위해 인덱스를 도착 칸으로 조정
        if (idx < 21 && next >= 21) {
            next = 21;
        }

        if (idx < 30 && next >= 30) {
            next = 30;
        }

        if (idx < 38 && next >= 38) {
            next = 38;
        }

        if (idx < 47 && next >= 47) {
            next = 47;
        }

        // 파란색 칸일 경우 빨간색 화살표를 타야되기 때문에 해당 인덱스로 바꿔줌
        if (next == 5) {
            return 22;
        }

        if (next == 10) {
            return 31;
        }

        if (next == 15) {
            return 39;
        }

        return next;
    }

    // 말이 있는 위치 방문처리 (윷놀이 판에 같은 자리인데 인덱스가 다른 경우가 있기 때문에 필요)
    private static void setVisited(int idx, boolean check) {
        // 도착 지점으로 가는 25, 30, 35, 40이 모든 경로에서 숫자가 겹침

        if (idx == 26 || idx == 34 || idx == 43) { // 25인 경우
            visited[26] = check;
            visited[34] = check;
            visited[43] = check;
        } else if (idx == 27 || idx == 35 || idx == 44) { // 30인 경우
            visited[27] = check;
            visited[35] = check;
            visited[44] = check;
        } else if (idx == 28 || idx == 36 || idx == 45) { // 35인 경우
            visited[28] = check;
            visited[36] = check;
            visited[45] = check;
        } else if (idx == 20 || idx == 29 || idx == 37 || idx == 46) { // 40인 경우
            visited[20] = check;
            visited[29] = check;
            visited[37] = check;
            visited[46] = check;
        } else {
            visited[idx] = check;
        }
    }
}