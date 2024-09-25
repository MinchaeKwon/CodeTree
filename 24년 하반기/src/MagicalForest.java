/**
 * 마법의 숲 탐색
 * 24 상 오후 1번
 *
 * @author minchae
 * @date 2024. 9. 25. 22:20 ~ 23:40
 * 
 * 메모리 12MB  시간 153ms
 */

import java.io.*;
import java.util.*;

public class MagicalForest {

	static class Pair {
		int x;
		int y;

		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}

	// 북동남서 (상우하좌)
	static int[] dx = { -1, 0, 1, 0 };
	static int[] dy = { 0, 1, 0, -1 };

	static int R, C, K;
	
	static int[][] map; // 정령 번호 저장
	static boolean[][] exit; // 출구 저장
	
	static int answer = 0;

	public static void main(String[] args) throws IOException {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringTokenizer st = new StringTokenizer(br.readLine());

		R = Integer.parseInt(st.nextToken());
		C = Integer.parseInt(st.nextToken());
		K = Integer.parseInt(st.nextToken());

		map = new int[R + 3][C]; // 골렘 세로 길이가 3이기 때문에 행 길이를 3 증가
		exit = new boolean[R + 3][C];

		for (int i = 1; i <= K; i++) {
			st = new StringTokenizer(br.readLine());

			int c = Integer.parseInt(st.nextToken()) - 1;
			int d = Integer.parseInt(st.nextToken());

			move(i, 0, c, d);
		}

		System.out.println(answer);
	}

	// 내려갈 수 있는 곳까지 내려감
	private static void move(int id, int x, int y, int d) {
		if (checkMove(x + 1, y)) {
			// 아래로 내려갈 수 있는 경우
			move(id, x + 1, y, d);
		} else if (checkMove(x + 1, y - 1)) {
			// 서쪽 아래로 내려갈 수 있는 경우 -> 반시계 방향으로 회전
			move(id, x + 1, y - 1, (d + 3) % 4);
		} else if (checkMove(x + 1, y + 1)) {
			// 동쪽 아래로 내려갈 수 있는 경우 -> 시계 방향으로 회전
			move(id, x + 1, y + 1, (d + 1) % 4);
		} else {
			// 더이상 내려갈 수 없는 경우
			
			// 골렘이 숲에 위치하는지 확인
			if (!isRange(x - 1, y) || !isRange(x + 1, y)) {
				// 정령 위아래로 범위를 벗어나는 경우 맵 초기화
				reset();
			} else {
				// 골렘 정착 -> 현재 위치와 4방향에 표시
				map[x][y] = id;
				
				for (int i = 0; i < 4; i++) {
					int nx = x + dx[i];
					int ny = y + dy[i];
					
					map[nx][ny] = id;
				}
				
				// 현재 정령 위치에 현재 방향만큼 더해주면 출구임
				exit[x + dx[d]][y + dy[d]] = true;
				
				answer += bfs(x, y);
			}
		}
	}

	// 해당 칸으로 이동할 수 있는지 확인 (정령의 위치)
	private static boolean checkMove(int x, int y) {
		// 일단 isMove가 true이면 정령 윗칸은 무조건 위치 가능한 것
		// 양 옆과 아래로 갔을 때 다른 골렘이 있는지만 확인하면 됨
		// move함수에서 현재 위치에서 다음 칸으로 내려갈 수 있는지 확인하고 내리기 때문에 현재 + 다음 위치 둘 다 확인
		return isMove(x, y) && map[x - 1][y - 1] == 0 && map[x - 1][y] == 0 && map[x - 1][y + 1] == 0
				&& map[x][y - 1] == 0 && map[x][y] == 0 && map[x][y + 1] == 0 && map[x + 1][y] == 0;
	}
	
	// 맵 초기화
	private static void reset() {
		for (int i = 0; i < R + 3; i++) {
			Arrays.fill(map[i], 0);
			Arrays.fill(exit[i], false);
		}
	}

	// 정령이 어디까지 내려갈 수 있는지 확인
	private static int bfs(int x, int y) {
		Queue<Pair> q = new LinkedList<>();
		boolean[][] visited = new boolean[R + 3][C];
		
		q.add(new Pair(x, y));
		visited[x][y] = true;
		
		int result = x; // 최솟값은 현재 행 크기
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				// 범위 벗어남, 이미 방문, 골렘이 없는 경우
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 0) {
					continue;
				}
				
				// 같은 골렘이거나 현재 출구인데 다음 칸이 다른 골렘인 경우
				if (map[nx][ny] == map[cur.x][cur.y] || (exit[cur.x][cur.y] && map[nx][ny] > 0)) {
					q.add(new Pair(nx, ny));
					visited[nx][ny] = true;
					
					result = Math.max(result, nx);
				}
			}
		}
		
		// 맵의 크기를 3칸 늘려주고 인덱스가 0부터 시작하기 때문에 -3 해주고 1 더해줌
		return result - 3 + 1;
	}

	// 골렘이 특정 칸으로 이동 가능한지 확인 -> 맵의 범위 안에 있으면 가능
	private static boolean isMove(int x, int y) {
		return x - 1 >= 0 && x + 1 < R + 3 && y - 1 >= 0 && y + 1 < C;
	}

	// 골렘 전체가 숲의 범위 벗어나지 않는지 확인
	private static boolean isRange(int x, int y) {
		return x >= 3 && x < R + 3 && y >= 0 && y < C;
	}

}
