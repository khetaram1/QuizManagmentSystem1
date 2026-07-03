// keeps track of how much time is left for one question
// once it hits 5 seconds it should flip into "danger mode"
public class QuizTimer {

	private int totalTime;
	private int timeLeft;

	public QuizTimer(int totalTime) {
		this.totalTime = totalTime;
		this.timeLeft = totalTime;
	}

	public void tick() {
		if (timeLeft > 0) {
			timeLeft--;
		}
	}

	public int getTimeLeft() {
		return timeLeft;
	}

	public int getTotalTime() {
		return totalTime;
	}

	public boolean isFinished() {
		return timeLeft <= 0;
	}

	// this is the line my mam was talking about - last 5 seconds = danger
	public boolean isDanger() {
		return timeLeft <= 5;
	}

	public boolean isWarning() {
		return timeLeft <= 15 && timeLeft > 5;
	}

	public String getFormatted() {
		int m = timeLeft / 60;
		int s = timeLeft % 60;
		return String.format("%02d:%02d", m, s);
	}
}
