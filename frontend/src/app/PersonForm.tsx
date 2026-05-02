
import React, {useEffect, useState} from 'react';
type Node = { id: number; name: string, type: string };


export default function PersonForm({ refresh } :any) {

    const [nodes, setNodes] = useState<Node[]>([]);
    const [selectedPersonId, setSelectedPersonId] = useState<number>(0);
    const [selectedManagerId, setSelectedManagerId] = useState<number>(0);
    const [selectedCompanyId, setSelectedCompanyId] = useState<number>(0);
    const [selectedDeptId, setSelectedDeptId] = useState<number>(0);
    const [selectedTeamId, setSelectedTeamId] = useState<number>(0);
    const [selectedProjectId, setSelectedProjectId] = useState<number>(0);
    const [newName, setNewName] = useState<string>('');

    const fetchNodes = () => {
        fetch('/api/node')
        .then(res => res.json())
        .then((json: Node[]) => {
            setNodes(json);
            if (json.length && !selectedPersonId) setSelectedPersonId(json.filter(n => n.type === 'Person')[0].id);
            if (json.length && !selectedManagerId) setSelectedManagerId(json.filter(n => n.type === 'Person')[0].id);
            if (json.length && !selectedCompanyId) setSelectedCompanyId(json.filter(n => n.type === 'Company')[0].id);
            if (json.length && !selectedDeptId) setSelectedDeptId(json.filter(n => n.type === 'Dept')[0].id);
            if (json.length && !selectedTeamId) setSelectedTeamId(json.filter(n => n.type === 'Team')[0].id);
            if (json.length && !selectedProjectId) setSelectedProjectId(json.filter(n => n.type === 'Project')[0].id);

        })
        .catch(err => console.error('Error fetching nodes:', err));
    };

   

    const handleSubmit = async (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!newName) return;
        try {
            const res = await fetch('/api/node', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json; charset=UTF-8'
                },
                body: JSON.stringify({
                    "name":encodeURIComponent(newName),
                    "type": "Person"
                })
            });
            const text = await res.text();
            console.log('create-node-and-relation:', text);
            setNewName('');
            refresh();
            fetchNodes();
        } catch (err) {
            console.error('Error creating node and relation:', err);
        }
    };

    const deleteNode = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedPersonId) return;
        fetch(`/api/node/${selectedPersonId}`, {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            }
        }).then(res => res.text())
        .then(text => {
            console.log('delete-node:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error deleting node:', err));
    }

    const linkPersonToCompany = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        console.log('selectedPersonId:', selectedPersonId);
        console.log('selectedCompanyId:', selectedCompanyId);
        if (!selectedPersonId || !selectedCompanyId) return;

        fetch('/api/edge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "nodeId1": selectedPersonId,
                "nodeId2": selectedCompanyId,
                "relationshipType": "WORKS_AT"
            })
        }).then(res => res.text())
        .then(text => {
            console.log('create-link:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error creating link:', err));
    };

    const linkPersonToDept = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!selectedPersonId || !selectedDeptId) return;
        fetch('/api/edge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "nodeId1": selectedPersonId,
                "nodeId2": selectedDeptId,
                "relationshipType": "WORKS_IN_DEPT"
            })
        }).then(res => res.text())
        .then(text => {
            console.log('create-link:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error creating link:', err));
    };

    const linkPersonToTeam = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();

        if (!selectedPersonId || !selectedTeamId) return;
        fetch('/api/edge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "nodeId1": selectedPersonId,
                "nodeId2": selectedTeamId,
                "relationshipType": "MEMBER_OF_TEAM"
            })
        }).then(res => res.text())
        .then(text => {
            console.log('create-link:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error creating link:', err));
    };

    const linkPersonToProject = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedPersonId || !selectedProjectId) return;
        fetch('/api/edge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "nodeId1": selectedPersonId,
                "nodeId2": selectedProjectId,
                "relationshipType": "MEMBER_OF_PROJECT"
            })
        }).then(res => res.text())
        .then(text => {
            console.log('create-link:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error creating link:', err));
    };

    const linkPersonToManager = (e: React.SubmitEvent<HTMLFormElement>) => {
        e.preventDefault();
        if (!selectedPersonId || !selectedManagerId) return;
        fetch('/api/edge', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json; charset=UTF-8'
            },
            body: JSON.stringify({
                "nodeId1": selectedPersonId,
                "nodeId2": selectedManagerId,
                "relationshipType": "REPORTS_TO"
            })
        }).then(res => res.text())
        .then(text => {
            console.log('create-link:', text);
            fetchNodes();
            refresh();
        }).catch(err => console.error('Error creating link:', err));
    };

    useEffect(() => {
        fetchNodes();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, []);

    return (
        <div>
            <details>
                <summary className="summary">Add Person</summary>
                <form onSubmit={handleSubmit} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label><input value={newName} onChange={e => setNewName(e.target.value)} placeholder="Person name" type="text" style={{width: 'stretch', padding: '10px'}}/> </label>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'seagreen', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Person</button>
                </form>
            </details>
            <details>
                <summary className="summary">Create Company Edge</summary>
                <form onSubmit={linkPersonToCompany} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select company: </label>
                    <select value={selectedCompanyId} onChange={e => setSelectedCompanyId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Company').map(c => ( <option key={c.id} value={c.id}>{c.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'steelblue', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Edge</button>
                </form>
        
            </details>
            <details>
                <summary className="summary">Create Dept Edge</summary>
                <form onSubmit={linkPersonToDept} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select dept: </label>
                    <select value={selectedDeptId} onChange={e => setSelectedDeptId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Dept').map(d => ( <option key={d.id} value={d.id}>{d.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'steelblue', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Edge</button>
                </form>
            </details>
            <details>
                <summary className="summary">Create Team Edge</summary>
                <form onSubmit={linkPersonToTeam} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select team: </label>
                    <select value={selectedTeamId} onChange={e => setSelectedTeamId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Team').map(t => ( <option key={t.id} value={t.id}>{t.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'steelblue', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Edge</button>
                </form>
            </details>
            <details>
                <summary className="summary">Create Project Edge</summary>
                <form onSubmit={linkPersonToProject} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select project: </label>
                    <select value={selectedProjectId} onChange={e => setSelectedProjectId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Project').map(pr => ( <option key={pr.id} value={pr.id}>{pr.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'steelblue', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Edge</button>
                </form>
            </details>
            <details>
                <summary className="summary">Create Manager Edge</summary>
                <form onSubmit={linkPersonToManager} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select manager: </label>
                    <select value={selectedManagerId} onChange={e => setSelectedManagerId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(m => ( <option key={m.id} value={m.id}>{m.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'steelblue', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Create Edge</button>
                </form>
            </details>

            <details>
                <summary style={{ padding: 12, backgroundColor: 'cadetblue', opacity: '70%', paddingLeft: '20px', cursor: 'pointer'}}>Delete Person</summary>
                <form onSubmit={deleteNode} style={{padding: 12, display: 'flex', gap: 8, flexDirection: 'column'}}>
                    <label style={{display: 'flex', gap: 6, alignItems: 'center'}}> Select person: </label>
                    <select value={selectedPersonId} onChange={e => setSelectedPersonId(Number(e.target.value))} style={{width: 'stretch', padding: '10px'}}> {nodes.filter(n => n.type === 'Person').map(p => ( <option key={p.id} value={p.id}>{p.name}</option> ))} </select>
                    <button type="submit" style={{width: 'stretch', backgroundColor: 'indianred', borderRadius: '5px', padding: '10px', marginTop: '10px'}}>Delete Person</button>
                </form>
            </details>

        </div>
    );
}