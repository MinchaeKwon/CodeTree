/**
 * 코드트리 빵
 * 
 * @author minchae
 * @date 2024. 3. 29.
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.Queue;
import java.util.StringTokenizer;

public class Retry1 {
	
	static class Pair {
		int x;
		int y;
		int d;
		
		public Pair(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public Pair(int x, int y, int d) {
			this.x = x;
			this.y = y;
			this.d = d;
		}
	}
	
	// 상좌우하 -> 방향 우선순위
	static int[] dx = {-1, 0, 0, 1};
	static int[] dy = {0, -1, 1, 0};
	
	static int n, m;
	static int[][] map; // 0 빈 칸, 1 베이스캠프, 2 갈 수 없는 곳
	
	static Pair[] person;
	static Pair[] cvs; // 편의점 위치
	
	static int time;

	public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        map = new int[n][n];
        person = new Pair[m];
        cvs = new Pair[m];
        
        for (int i = 0; i < n; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	for (int j = 0; j < n; j++) {
        		map[i][j] = Integer.parseInt(st.nextToken());
        	}
        }
        
        for (int i = 0; i < m; i++) {
        	st = new StringTokenizer(br.readLine());
        	
        	int x = Integer.parseInt(st.nextToken()) - 1;
        	int y = Integer.parseInt(st.nextToken()) - 1;
        	
        	cvs[i] = new Pair(x, y);
        	person[i] = new Pair(-1, -1);
        }

        while (true) {
        	time++;
        	
        	moveCvs();
        	
        	if (time <= m) {
        		moveBaseCamp();
    		}
        	
        	if (isEnd()) {
        		break;
        	}
        }
        
        System.out.println(time);
	}
	
	private static void moveCvs() {
		// 모든 사람들이 본인이 가고싶은 편의점 방향으로 한 칸 움직임
		for (int i = 0; i < m; i++) {
			// 격자 밖이거나 이미 편의점에 도착한 경우 다음으로 넘어감
			if (!isRange(person[i].x, person[i].y) || (person[i].x == cvs[i].x && person[i].y == cvs[i].y)) {
				continue;
			}
			
			int d = bfs(person[i], cvs[i]);
			
			person[i].x += dx[d];
			person[i].y += dy[d];
		}
		
		// 편의점으로 이동한 사람이 있을 경우 해당 편의점에 이동할 수 없다는 표시를 해줌
		// 모든 사람들이 이동한 뒤에 해줌
		for (int i = 0; i < m; i++) {
			if (person[i].x == cvs[i].x && person[i].y == cvs[i].y) {
				map[person[i].x][person[i].y] = 2;
			}
		}
	}
	
	// 특정 위치까지의 최단 거리 구함
	private static int bfs(Pair start, Pair end) {
		Queue<Pair> q = new LinkedList<>();
		boolean[][] visited = new boolean[n][n];

		q.add(new Pair(start.x, start.y, -1));
		visited[start.x][start.y] = true;

		while (!q.isEmpty()) {
			Pair cur = q.poll();

			if (cur.x == end.x && cur.y == end.y) {
				return cur.d;
			}

			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];

				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 2) {
					continue;
				}
				
				/*
				 * 본인이 가고싶은 편의점에 최단거리로 갈 수 있는 방향으로 한 칸 움직이기 때문에 처음 이동한 방향을 그대로 저장함
				 * 방향이 정해지지 않은 경우 현재 이동방향 저장하고, 방향이 있는 경우에는 해당 방향 그대로 저장
				 */
				q.add(new Pair(nx, ny, cur.d == -1 ? i : cur.d));

				/*
				 * 처음에 이동하는대로 방향을 다 새로 저장하고 편의점 발견한 경우 그 방향을 반환함 
				 * 이렇게 하면 편의점 이전 칸에서 편의점으로 이동하는 방향이 저장됨 -> 시간초과 발생
				 * 한 칸만 움직이는 것이기 때문에 처음 정해진 방향을 그대로 저장해야함 -> 위에서의 코드
				 */
//				q.add(new Pair(nx, ny, i));
				
				visited[nx][ny] = true;
			}
		}

		return 0;
	}
	
	// time번 사람이 자신이 가고싶은 편의점과 가까운 베이스캠프로 이동
	private static void moveBaseCamp() {
		Queue<Pair> q = new LinkedList<>();
		boolean[][] visited = new boolean[n][n];
		
		q.add(cvs[time - 1]);
		visited[cvs[time - 1].x][cvs[time - 1].y] = true;
		
		while (!q.isEmpty()) {
			Pair cur = q.poll();
			
			// 베이스캠프 발견한 경우 사람 이동 시킴
			if (map[cur.x][cur.y] == 1) {
				person[time - 1] = new Pair(cur.x, cur.y);
				map[cur.x][cur.y] = 2; // 갈 수 없는 표시 해줌
				
				return;
			}
			
			for (int i = 0; i < 4; i++) {
				int nx = cur.x + dx[i];
				int ny = cur.y + dy[i];
				
				if (!isRange(nx, ny) || visited[nx][ny] || map[nx][ny] == 2) {
					continue;
				}
				
				q.add(new Pair(nx, ny, i));
				visited[nx][ny] = true;
			}
		}
	}
	
	// 모든 사람들이 편의점에 도착했는지 확인
	private static boolean isEnd() {
		for (int i = 0; i < m; i++) {
			if (!(person[i].x == cvs[i].x && person[i].y == cvs[i].y)) {
				return false;
			}
		}
		
		return true;
	}
	
	private static boolean isRange(int x, int y) {
		return x >= 0 && x < n && y >= 0 && y < n;
	}

}
