import logo from './logo.svg';
import './App.css';
import Registro from './components/Registro';

function App() {
  return (
    <div className="App">
      <header className="App-header">
        <h1>Bem-vindo ao kanban</h1>
        <Registro/> {/* <-- 2. Renderize nosso componente aqui */}
      </header>
    </div>
  );
}

export default App;
