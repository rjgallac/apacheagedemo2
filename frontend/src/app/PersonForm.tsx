
import React, {useEffect, useState} from 'react';
type Person = { id: number; name: string };


export default function PersonForm() {
    const [people, setPeople] = useState<Person[]>([]);
    const [selectedId, setSelectedId] = useState<string>('');
    const [newName, setNewName] = useState<string>('');
    const fetchPeople = () => {
        fetch('/api/person')
        .then(res => res.json())
        .then((json: Person[]) => {
            setPeople(json);
            if (json.length && !selectedId) setSelectedId(String(json[0].id));
        })
        .catch(err => console.error('Error fetching people:', err));
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!selectedId || !newName) return;
        try {
            const res = await fetch('/api/person/create-node-and-relation', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "name":encodeURIComponent(newName),
                    "managerId": selectedId
                })
            });
            const text = await res.text();
            console.log('create-node-and-relation:', text);
            setNewName('');
            //   fetchGraph();
            fetchPeople();
        } catch (err) {
            console.error('Error creating node and relation:', err);
        }
    };

    useEffect(() => {
        fetchPeople();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div>
            <h4>Add Person</h4>

            <form onSubmit={handleSubmit} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                <label>
                    <input value={newName} onChange={e => setNewName(e.target.value)} placeholder="Person name" type="text" style={{width: 'stretch', padding: '10px'}}/>
                </label>

                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Person</button>
            </form>

            <form onSubmit={handleSubmit} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                <label>
                Select person:
                <select value={selectedId} onChange={e => setSelectedId(e.target.value)} style={{width: 'stretch', padding: '10px'}}>
                    {people.map(p => (
                    <option key={p.id} value={String(p.id)}>{p.name}</option>
                    ))}
                </select>
                </label>

                <label >
                New name:
                <input value={newName} onChange={e => setNewName(e.target.value)} placeholder="Name for new node" style={{width: 'stretch', padding: '10px'}} />
                </label>

                <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create node + relation</button>
            </form>

            <h4>Delete Person</h4>
            <form onSubmit={e => {
                e.preventDefault();
                if (!selectedId) return;
                fetch(`/api/person/${selectedId}`, {
                    method: 'DELETE',
                    headers: {
                        'Content-Type': 'application/json; charset=UTF-8'
                    }
                }).then(res => res.text())
                .then(text => {
                    console.log('delete-person:', text);
                    fetchPeople();
                })
                .catch(err => console.error('Error deleting person:', err));
            }} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                <label style={{display: 'flex', gap: 6, alignItems: 'center'}}>
                    Select person:
                </label>

                <select value={selectedId} onChange={e => setSelectedId(e.target.value)} style={{width: 'stretch', padding: '10px'}}>
                    {people.map(p => (
                    <option key={p.id} value={String(p.id)}>{p.name}</option>
                    ))}
                </select>
                <button type="submit" style={{width: 'stretch', backgroundColor: 'indianred', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Person</button>
            </form>
        </div>
    );
}