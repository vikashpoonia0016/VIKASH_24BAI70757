import "react-toastify/dist/ReactToastify.css";
import { ToastContainer } from "react-toastify";
import PostComposer from "./components/PostComposer";
import "./styles.css";

export default function App() {
  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Post Composer</h1>
        <p className="subtitle">
          Unit 1 · Experiment 1.1 — Post Composer with Platform Validation
        </p>
      </header>

      <main className="app-main app-main-single">
        <PostComposer />
      </main>

      <ToastContainer position="bottom-right" autoClose={2500} theme="colored" />
    </div>
  );
}
