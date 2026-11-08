import { useState, useMemo } from "react";
import { toast } from "react-toastify";
import {
  getPlatformList,
  getLimit,
  validateForPlatform,
} from "../utils/validationStrategies";

const platforms = getPlatformList();

/**
 * Post Composer (Experiment 1.1)
 * --------------------------------
 * - Controlled components: textarea/select values are driven by state
 * - Platform selection dropdown
 * - Dynamic character limits per platform
 * - Real-time validation using the Strategy Pattern (see validationStrategies.js)
 * - Clear error messages, and blocks "posting" until content is valid
 */
export default function PostComposer() {
  const [platform, setPlatform] = useState(platforms[0].key);
  const [content, setContent] = useState("");
  const [error, setError] = useState("");
  const [lastPosted, setLastPosted] = useState(null);

  const limit = useMemo(() => getLimit(platform), [platform]);
  const remaining = limit - content.length;

  const handleContentChange = (e) => {
    const value = e.target.value;
    setContent(value);
    const result = validateForPlatform(platform, value);
    setError(result.valid ? "" : result.message);
  };

  const handlePlatformChange = (e) => {
    const newPlatform = e.target.value;
    setPlatform(newPlatform);
    const result = validateForPlatform(newPlatform, content);
    setError(result.valid ? "" : result.message);
  };

  const handleClear = () => {
    setContent("");
    setError("");
  };

  const handlePost = () => {
    const result = validateForPlatform(platform, content);
    if (!result.valid) {
      setError(result.message);
      toast.error(result.message);
      return;
    }
    setLastPosted({ platform, content });
    toast.success(`Post validated and ready for ${platforms.find(p => p.key === platform).label}!`);
  };

  return (
    <div className="card">
      <h2>Compose Post</h2>

      <label className="field-label" htmlFor="platform-select">
        Platform
      </label>
      <select
        id="platform-select"
        value={platform}
        onChange={handlePlatformChange}
        className="select"
      >
        {platforms.map((p) => (
          <option key={p.key} value={p.key}>
            {p.label} (limit: {p.limit})
          </option>
        ))}
      </select>

      <label className="field-label" htmlFor="content-area">
        Content
      </label>
      <textarea
        id="content-area"
        className={`textarea ${error ? "textarea-error" : ""}`}
        value={content}
        onChange={handleContentChange}
        placeholder="Write your post..."
        rows={8}
      />

      <div className="meta-row">
        <span className={remaining < 0 ? "count-negative" : "count"}>
          {remaining} characters remaining
        </span>
        {error && <span className="error-msg">{error}</span>}
      </div>

      <div className="button-row">
        <button className="btn btn-primary" onClick={handlePost}>
          Validate &amp; Post
        </button>
        <button className="btn btn-ghost" onClick={handleClear}>
          Clear
        </button>
      </div>

      {lastPosted && (
        <div className="last-posted">
          <strong>Last validated post</strong> (
          {platforms.find((p) => p.key === lastPosted.platform)?.label}):
          <p>{lastPosted.content}</p>
        </div>
      )}
    </div>
  );
}
