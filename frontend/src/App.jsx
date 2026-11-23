import { useEffect, useState } from "react";
import "./App.css";

function App() {
  const [offices, setOffices] = useState([]);
  const [search, setSearch] = useState("");
  const [error, setError] = useState("");

  const loadOffices = async () => {
    try {
      const response = await fetch("/api/offices/all");
      if (!response.ok) throw new Error("Failed to load offices");

      const data = await response.json();
      setOffices(data);
      setError("");
    } catch (error) {
      setError(error.message);
    }
  };

  useEffect(() => {
    loadOffices();
  }, []);

  const deleteOffice = async (id) => {
    if (!window.confirm("Are you sure you want to delete this office?")) {
      return;
    }

    try {
      const response = await fetch(`/api/offices/delete/${id}`, {
        method: "DELETE",
      });

      if (!response.ok) throw new Error("Failed to delete office");

      alert("Office deleted successfully!");
      loadOffices();
    } catch (error) {
      alert(error.message);
    }
  };

  const filteredOffices = offices.filter((office) =>
    office.name?.toLowerCase().includes(search.toLowerCase()) ||
    String(office.officeNumber).includes(search.trim())
  );

  return (
    <div className="container">
      <h1>Clinic Office Management</h1>
      <p>View and manage available offices</p>

      <input
        type="text"
        placeholder="Search by office name or number..."
        value={search}
        onChange={(e) => setSearch(e.target.value)}
      />

      {error && <p className="error">{error}</p>}

      <table>
        <thead>
          <tr>
            <th>Office ID</th>
            <th>Office Name</th>
            <th>Office Number</th>
            <th>Actions</th>
          </tr>
        </thead>

        <tbody>
          {filteredOffices.map((office) => (
            <tr key={office.id}>
              <td>{office.id}</td>
              <td>{office.name}</td>
              <td>{office.officeNumber}</td>
              <td>
                <button className="update" disabled>
                  Update
                </button>

                <button
                  className="delete"
                  onClick={() => deleteOffice(office.id)}
                >
                  Delete
                </button>
              </td>
            </tr>
          ))}

          {filteredOffices.length === 0 && (
            <tr>
              <td colSpan="4">No offices found</td>
            </tr>
          )}
        </tbody>
      </table>
    </div>
  );
}

export default App;